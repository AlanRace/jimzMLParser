package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.Chromatogram;
import com.alanmrace.jimzmlparser.mzml.HasChildren;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.MzMLDataContainer;
import com.alanmrace.jimzmlparser.mzml.MzMLIndexedContentWithParams;
import com.alanmrace.jimzmlparser.mzml.MzMLOrderedContentWithParams;
import com.alanmrace.jimzmlparser.mzml.MzMLTag;
import com.alanmrace.jimzmlparser.mzml.MzMLTagList;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import com.alanmrace.jimzmlparser.util.HexHelper;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * Writer for exporting to mzML. Binary data exported as Base64 in the same file
 * as the metadata.
 *
 * @author Alan Race
 */
public class MzMLWriter implements MzMLWritable {

    /**
     * Encoding to write the XML document out as. Default is ISO-8859-1.
     */
    protected String encoding = "ISO-8859-1";

    /**
     * Location of the metadata file.
     */
    protected String metadataLocation;

    /**
     * RandomAccessFile for the file containing the XML metadata.
     */
    protected RandomAccessFile metadataRAF;

    /**
     * BufferedWriter created from the metadata RandomAccessFile for writing out
     * to.
     */
    protected BufferedWriter output;

    /**
     * Whether the mzML index should be written out.
     */
    protected boolean shouldOutputIndex;

//    protected int currentIndex;
    
    /**
     * Locations of each MzMLDataContainer (offset within the metadata file), for use 
     * when writing out the index.
     */
    private Map<MzMLDataContainer, Long> dataContainerLocations;
    
    /**
     * List of WriterListeners to be notified on progress of this writer.
     */
    private List<WriterListener> listeners = new ArrayList<WriterListener>();
    
    /**
     * Stack of index counters, used to add in both the 'index' and 'count' attributes
     * of different MzML tag elements.
     */
    private Deque<AtomicInteger> indexStack = new ArrayDeque<AtomicInteger>();
    
    /**
     * Current SHA-1 hash of the metadata file, for use when writing out the index.
     */
    private MessageDigest sha1HashDigest;

    /**
     * Set up default MzMLWriter. Default encoding is ISO-8859-1 and will output
     * mzML index.
     */
    public MzMLWriter() {
        shouldOutputIndex = true;
        encoding = "ISO-8859-1";
    }

    /**
     * Set up default MzMLWriter with specified option to output mzML index.
     * Default encoding is ISO-8859-1.
     *
     * @param shouldOutputIndex Whether the mzML index should be written out or
     * not
     */
    public MzMLWriter(boolean shouldOutputIndex) {
        this();

        this.shouldOutputIndex = shouldOutputIndex;
    }

    /**
     * Set up MzMLWriter with specified option to output mzML index and
     * encoding.
     *
     * @param shouldOutputIndex Output mzML index
     * @param encoding XML encoding
     */
    public MzMLWriter(boolean shouldOutputIndex, String encoding) {
        this();

        this.encoding = encoding;
        this.shouldOutputIndex = shouldOutputIndex;
    }

    @Override
    public String getMetadataLocation() {
        return metadataLocation;
    }
    
    /**
     * Indent the output by the specified number of spaces (indent).
     *
     * @param output BufferedReader to output the indents to
     * @param indent Number of tabs to indent
     * @throws IOException Exception occurred during writing data
     */
    public static void indent(MzMLWritable output, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            output.writeMetadata("  ");
        }
    }

    @Override
    public void write(MzML mzML, String outputLocation) throws IOException {
        notifyStart();
        
        this.metadataLocation = outputLocation;
        dataContainerLocations = new HashMap<MzMLDataContainer, Long>();

        metadataRAF = new RandomAccessFile(metadataLocation, "rw");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(metadataRAF.getFD()), encoding);

        output = new BufferedWriter(out);

        if (shouldOutputIndex()) {
            try {
                sha1HashDigest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(MzMLWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        writeMetadata("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");

        int indent = 0;

        if (shouldOutputIndex()) {
            MzMLWriter.indent(this, indent);
            writeMetadata("<indexedmzML");
            writeMetadata(" xmlns=\"" + MzML.NAMESPACE + "\"");
            writeMetadata(" xmlns:xsi=\"" + MzML.XSI + "\"");
            writeMetadata(" xsi:schemaLocation=\"" + MzML.IDX_SCHEMA_LOCATION + "\">\n");

            indent++;
        }

        outputXML(mzML, indent);

        if (shouldOutputIndex()) {
            indent--;

            output.flush();
            long indexListOffset = getMetadataPointer();

            MzMLWriter.indent(this, indent + 1);
            writeMetadata("<indexList count=\"1\">\n");
            MzMLWriter.indent(this, indent + 2);
            writeMetadata("<index name=\"spectrum\">\n");

            for (Spectrum spectrum : mzML.getRun().getSpectrumList()) {
                MzMLWriter.indent(this, indent + 3);
                writeMetadata("<offset idRef=\"" + spectrum.getID() + "\">" + dataContainerLocations.get(spectrum) + "</offset>\n");
            }

            MzMLWriter.indent(this, indent + 2);
            writeMetadata("</index>\n");
            
            if(mzML.getRun().getChromatogramList() != null && mzML.getRun().getChromatogramList().size() > 0) {
                MzMLWriter.indent(this, indent + 2);
                writeMetadata("<index name=\"chromatogram\">\n");

                for (Chromatogram chromatogram : mzML.getRun().getChromatogramList()) {
                    MzMLWriter.indent(this, indent + 3);
                    writeMetadata("<offset idRef=\"" + chromatogram.getID() + "\">" + dataContainerLocations.get(chromatogram) + "</offset>\n");
                }

                MzMLWriter.indent(this, indent + 2);
                writeMetadata("</index>\n");
            }
            
            MzMLWriter.indent(this, indent + 1);
            writeMetadata("</indexList>\n");

            MzMLWriter.indent(this, indent + 1);
            writeMetadata("<indexListOffset>" + indexListOffset + "</indexListOffset>\n");

            MzMLWriter.indent(this, indent + 1);
            writeMetadata("<fileChecksum>");
            // Write out directly
            writeMetadata(HexHelper.byteArrayToHexString(sha1HashDigest.digest()));
            writeMetadata("</fileChecksum>\n");
            MzMLWriter.indent(this, indent + 1);
            
            MzMLWriter.indent(this, indent);
            writeMetadata("</indexedmzML>\n");
        }

        output.flush();
        metadataRAF.setLength(metadataRAF.getFilePointer());

        output.close();
        
        notifyEnd();
    }
    
    private void notifyStart() {
        for(WriterListener listener : listeners)
            listener.start();
    }
    
    private void notifyEnd() {
        for(WriterListener listener : listeners)
            listener.end();
    }
    
    private void updateProgress(long current) {
        for(WriterListener listener : listeners)
            listener.progress(current);
    }

    protected void outputXML(MzMLTag tag, int indent) throws IOException {
        String attributeText = tag.getXMLAttributeText();

        if(tag instanceof MzMLDataContainer)
            dataContainerLocations.put((MzMLDataContainer) tag, this.getMetadataPointer());
        
        MzMLWriter.indent(this, indent);
        writeMetadata("<" + tag.getTagName());

        if (attributeText != null && !attributeText.isEmpty()) {
            writeMetadata(" " + attributeText);
        }

        if (tag instanceof MzMLIndexedContentWithParams) {
            writeMetadata(" index=\"" + indexStack.peekLast().getAndIncrement() + "\"");
        } else if (tag instanceof MzMLOrderedContentWithParams) {
            writeMetadata(" order=\"" + indexStack.peekLast().incrementAndGet() + "\"");
        }
        
        if(tag instanceof MzMLDataContainer)
            updateProgress(indexStack.peekLast().get());

        if (!(tag instanceof HasChildren)) {
            writeMetadata("/>\n");
        } else {
            writeMetadata(">\n");

            outputXMLContent((HasChildren) tag, indent + 1);

            MzMLWriter.indent(this, indent);
            writeMetadata("</" + tag.getTagName() + ">\n");
        }
    }

    protected void outputXMLContent(HasChildren tag, int indent) throws IOException {
        ArrayList<MzMLTag> children = new ArrayList<MzMLTag>();

        tag.addChildrenToCollection(children);

        // If the tag is a list, reset the counter for index / order
        if (tag instanceof MzMLTagList) {
            indexStack.push(new AtomicInteger(0));
        }

        for (MzMLTag child : children) {
            outputXML(child, indent);
        }

        if (tag instanceof BinaryDataArray) {
            //CVParam externalData = ((BinaryDataArray) tag).getCVParam(BinaryDataArray.externalDataID);

            //if (externalData == null) {
            MzMLWriter.indent(this, indent);
            writeBinaryTag((BinaryDataArray) tag);
            //}
        }
        
        if (tag instanceof MzMLTagList) {
            indexStack.pop();
        }
    }

    protected void writeBinaryTag(BinaryDataArray bda) throws IOException {
        double[] data = bda.getDataAsDouble();

        if (data == null) {
            writeMetadata("<binary />\n");
        } else {
            byte[] byteData = prepareData(data, bda);
//            System.out.println(byteData);
            writeMetadata("<binary>");
            writeData(byteData);
            writeMetadata("</binary>\n");
        }
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        writeMetadata(Base64.encode(data));
    }

    @Override
    public byte[] prepareData(double[] data, BinaryDataArray binayDataArray) throws IOException {
        DataTransformation transformation = binayDataArray.generateDataTransformation();
        byte[] byteData = null;

        if (transformation != null) {
            try {
                byteData = transformation.performForwardTransform(data);
            } catch (DataFormatException ex) {
                Logger.getLogger(MzMLWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return byteData;
    }

    @Override
    public void writeMetadata(String str) throws IOException {
        if (shouldOutputIndex() && sha1HashDigest != null) {
            // TODO: pass str to SHA1 checksum
            sha1HashDigest.update(str.getBytes(encoding));
        }
        
        output.write(str);
    }

    @Override
    public void flush() throws IOException {
        output.flush();
    }

    @Override
    public void close() throws IOException {
        output.flush();

        metadataRAF.getChannel().truncate(metadataRAF.getFilePointer());
        output.close();
    }

    @Override
    public long getMetadataPointer() throws IOException {
        output.flush();

        return metadataRAF.getFilePointer();
    }

    @Override
    public long getDataPointer() throws IOException {
        output.flush();

        return metadataRAF.getFilePointer();
    }

    @Override
    public boolean shouldOutputIndex() {
        return shouldOutputIndex;
    }

    @Override
    public void addListener(WriterListener listener) {
        this.listeners.add(listener);
    }
}
