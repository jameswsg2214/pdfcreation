package com.wils.pdfcreation;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Dummy {


    void main() throws FileNotFoundException {

/*
        Bitmap bitmap = ((BitmapDrawable) drawable ).getBitmap();

        float cl[] = {100,199,199};




        table.addCell(new Cell(3,1).add())


                Biy*/

//        newPdf();

//        Toast.makeText(this,"PDF created ",Toast.LENGTH_SHORT).show();
    }

    private void newPdf() throws IOException {

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        File file = new File(path,"myPdf.pdf");

        OutputStream outputStream = new FileOutputStream(file);


        PdfWriter writer = new PdfWriter(String.valueOf(file));

        PdfDocument pdfDocument = new PdfDocument(writer);

        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Hellloooooo");


        Paragraph header = new Paragraph("Copy")
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(14)
                .setFontColor(Color.RED);

        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++)
        {
            Rectangle pageSize = pdfDocument.getPage(i).getPageSize();
            float x = pageSize.getWidth() / 2;
            float y = pageSize.getTop() - 20;
            document.showTextAligned(header, x, y, i, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        }

        document.add(paragraph);

        document.close();


    }
}
