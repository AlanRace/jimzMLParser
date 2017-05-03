package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.HasChildren;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.MzMLContent;
import com.alanmrace.jimzmlparser.mzml.MzMLIndexedContentWithParams;
import com.alanmrace.jimzmlparser.mzml.MzMLOrderedContentWithParams;
import com.alanmrace.jimzmlparser.mzml.MzMLTag;
import com.alanmrace.jimzmlparser.mzml.MzMLTagList;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
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

    protected int currentIndex;

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

    @Override
    public void write(MzML mzML, String outputLocation) throws IOException {
        this.metadataLocation = outputLocation;

        metadataRAF = new RandomAccessFile(metadataLocation, "rw");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(metadataRAF.getFD()), encoding);

        output = new BufferedWriter(out);

        output.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");

        int indent = 0;

        if (shouldOutputIndex()) {
            MzMLContent.indent(this, indent);
            writeMetadata("<indexedmzML");
            writeMetadata(" xmlns=\"http://psi.hupo.org/ms/mzml\"");
            writeMetadata(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            writeMetadata(" xsi:schemaLocation=\"http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.2_idx.xsd\">\n");

            indent++;
        }

        outputXML(mzML, indent);

        if (shouldOutputIndex()) {
            indent--;

            output.flush();
            long indexListOffset = getMetadataPointer();

            MzMLContent.indent(this, indent + 1);
            writeMetadata("<indexList count=\"1\">\n");
            MzMLContent.indent(this, indent + 2);
            writeMetadata("<index name=\"spectrum\">\n");

            // TODO: Move this away from the Spectrum - this doesn't care where it 
            // is stored - this should be kept track of in the MzMLWriter
            for (Spectrum spectrum : mzML.getRun().getSpectrumList()) {
                MzMLContent.indent(this, indent + 3);
                writeMetadata("<offset idRef=\"" + spectrum.getID() + "\">" + spectrum.getmzMLLocation() + "</offset>\n");
            }

            MzMLContent.indent(this, indent + 2);
            writeMetadata("</index>\n");
            MzMLContent.indent(this, indent + 1);
            writeMetadata("</indexList>\n");

            MzMLContent.indent(this, indent + 1);
            writeMetadata("<indexListOffset>" + indexListOffset + "</indexListOffset>\n");

            MzMLContent.indent(this, indent);
            writeMetadata("</indexedmzML>\n");
        }

        output.flush();
        metadataRAF.setLength(metadataRAF.getFilePointer());

        output.close();
    }

    protected void outputXML(MzMLTag tag, int indent) throws IOException {
        String attributeText = tag.getXMLAttributeText();

        MzMLContent.indent(this, indent);
        writeMetadata("<" + tag.getTagName());

        if (attributeText != null && !attributeText.isEmpty()) {
            writeMetadata(" " + attributeText);
        }

        if (tag instanceof MzMLIndexedContentWithParams) {
            writeMetadata(" index=\"" + currentIndex++ + "\"");
        } else if (tag instanceof MzMLOrderedContentWithParams) {
            writeMetadata(" order=\"" + ++currentIndex + "\"");
        }

        if (!(tag instanceof HasChildren)) {
            writeMetadata("/>\n");
        } else {
            writeMetadata(">\n");

            outputXMLContent((HasChildren) tag, indent + 1);

            MzMLContent.indent(this, indent);
            writeMetadata("</" + tag.getTagName() + ">\n");
        }
    }

    protected void outputXMLContent(HasChildren tag, int indent) throws IOException {
        ArrayList<MzMLTag> children = new ArrayList<MzMLTag>();

        tag.addChildrenToCollection(children);

        // If the tag is a list, reset the counter for index / order
        if (tag instanceof MzMLTagList) {
            currentIndex = 0;
        }

        for (MzMLTag child : children) {
            outputXML(child, indent);
        }

        if (tag instanceof BinaryDataArray) {
            //CVParam externalData = ((BinaryDataArray) tag).getCVParam(BinaryDataArray.externalDataID);

            //if (externalData == null) {
            MzMLContent.indent(this, indent);
            writeBinaryTag((BinaryDataArray) tag);
            //}
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
}
