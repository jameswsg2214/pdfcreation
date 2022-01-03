    package com.wils.pdfcreation

    import android.Manifest
    import android.annotation.SuppressLint
    import android.app.AlertDialog
    import android.content.DialogInterface
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.graphics.*
    import android.net.Uri
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.os.Environment
    import android.util.Log
    import android.widget.Button
    import android.widget.Toast
    import androidx.core.content.ContextCompat
    import androidx.core.content.FileProvider
    import android.graphics.Typeface
    import android.graphics.drawable.BitmapDrawable
    import androidx.core.content.res.ResourcesCompat
    import android.graphics.pdf.PdfDocument
    import com.itextpdf.io.font.FontConstants


    import com.itextpdf.layout.property.VerticalAlignment
    import java.io.*
    import com.itextpdf.kernel.color.Color
    import com.itextpdf.kernel.font.PdfFontFactory
    import com.itextpdf.kernel.geom.Rectangle
    import com.itextpdf.kernel.pdf.PdfWriter
    import com.itextpdf.layout.Document
    import com.itextpdf.layout.element.Paragraph
    import com.itextpdf.layout.property.TextAlignment
    import com.itextpdf.io.image.ImageDataFactory
    import com.itextpdf.io.source.ByteArrayOutputStream
    import com.itextpdf.kernel.pdf.PdfReader
    import com.itextpdf.layout.border.Border
    import com.itextpdf.layout.element.Cell
    import com.itextpdf.layout.element.Image
    import com.itextpdf.layout.element.Table


    open class MainActivity : AppCompatActivity() {


        var pageHeight = 1120
        var pagewidth = 792
        var PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 200

        // creating a bitmap variable
        // for storing our images
        var bmp: Bitmap? = null
        // creating a bitmap variable // for storing our images
        var scaledbmp: Bitmap? = null

        var bmp2: Bitmap? = null
        // creating a bitmap variable // for storing our images
        var scaledbmp2: Bitmap? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)



            var btn = findViewById<Button>(R.id.btn)

            btn.setOnClickListener {

                runTimePermission()
            }


        }


        @SuppressLint("NewApi")
        private fun runTimePermission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
                )


                return
            }

            else{
                initPdf()
                return
            }
        }
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
                Log.i("", "" + grantResults)

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // now, you have permission go ahead
                    initPdf()

                } else {

                    getCustomDialog()
                }}

        }

        private fun getCustomDialog() {
            // build alert dialog
            val dialogBuilder = AlertDialog.Builder(this)
            // set message of alert dialog
            dialogBuilder.setMessage("App need this permission")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                    runTimePermission()
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Permission!!")
            // show alert dialog
            alert.show()
        }

        private fun initPdf() {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tamilnadu_logo);
            scaledbmp = Bitmap.createScaledBitmap(bmp!!, 120, 120, false);

            bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.nhm_logo_new_1);
            scaledbmp2 = Bitmap.createScaledBitmap(bmp2!!, 130, 120, false);
    //        generatePDF()

            newPdf()
    //        generatePDFNew()
        }


        @Throws(IOException::class)
        private fun newPdf() {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
            val file = File(path, "myPdf.pdf")

            val outputStream: OutputStream = FileOutputStream(file)
            val writer = PdfWriter(file.toString())
            val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(writer)
            val document = Document(pdfDocument)

            document.setMargins(120f,40f,90f,40f)

            val pageWidth  = pdfDocument.defaultPageSize.width
            val onePercentageOfWidth  = (pdfDocument.defaultPageSize.width)/10

            val columnOfFromToDate = floatArrayOf(onePercentageOfWidth,onePercentageOfWidth*3,onePercentageOfWidth*3,onePercentageOfWidth*3)
            val fromToDateTable:Table= Table(columnOfFromToDate).setMarginTop(10f)




            fromToDateTable.addCell(Cell(1,1).add(Paragraph("S.No")).setTextAlignment(TextAlignment.CENTER))
            fromToDateTable.addCell(Cell(2,1).add(Paragraph("Name")).setTextAlignment(TextAlignment.CENTER))
            fromToDateTable.addCell(Cell(3,1).add(Paragraph("Code")).setTextAlignment(TextAlignment.CENTER))
            fromToDateTable.addCell(Cell(4,1).add(Paragraph("Quantity")).setTextAlignment(TextAlignment.CENTER))
    //        fromToDateTable.addCell(Cell(2,1).add(Paragraph("To date :10-2-2021")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER))

            document.add(fromToDateTable)



            for(i in 1.. 100 ){


    //            val columnOfFromToDate = floatArrayOf(onePercentageOfWidth,onePercentageOfWidth*3,onePercentageOfWidth*3,onePercentageOfWidth*3)
                val listTable:Table= Table(columnOfFromToDate)

                listTable.addCell(Cell(1,1).add(Paragraph("S.No ${i}")).setTextAlignment(TextAlignment.CENTER))
                listTable.addCell(Cell(2,1).add(Paragraph("Name ${i}")).setTextAlignment(TextAlignment.CENTER))
                listTable.addCell(Cell(3,1).add(Paragraph("Code ${i}")).setTextAlignment(TextAlignment.CENTER))
                listTable.addCell(Cell(4,1).add(Paragraph("Quantity ${i}")).setTextAlignment(TextAlignment.CENTER))


                document.add(listTable)

            }

            document.close()
            ManipulatePdf(file.path,file)

        }

        protected fun ManipulatePdf(dest: String?,file: File) {


            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
            val fileNew = File(path, "myPdf2.pdf")

            val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(PdfReader(dest), PdfWriter(fileNew.path))
            val doc = Document(pdfDoc)

            for (i in 1..pdfDoc.numberOfPages) {
                val pageSize: Rectangle = pdfDoc.getPage(i).pageSize

                val pageWidth= pageSize.width
                val pageHeight= pageSize.height

                val drawable = getDrawable(R.drawable.tamilnadu_logo)
                val bitmap = (drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val bitmapData = stream.toByteArray()
                val imageData = ImageDataFactory.create(bitmapData)
                val image = Image(imageData)

                val drawable2 = getDrawable(R.drawable.nhm_logo_new_1)
                val bitmap2 = (drawable2 as BitmapDrawable).bitmap
                val stream2 = ByteArrayOutputStream()
                bitmap2.compress(Bitmap.CompressFormat.PNG,100,stream2)
                val bitmapData2 = stream2.toByteArray()
                val imageData2 = ImageDataFactory.create(bitmapData2)
                val image2 = Image(imageData2)

                image.scaleToFit((pageSize.width/10), (pageSize.width/10))
                image2.scaleToFit((pageSize.width/10), (pageSize.width/10))

                val center = pageWidth - (pageWidth/5)

                val cl = floatArrayOf((pageSize.width/10),center, (pageSize.width/10))

                var table:Table = Table(cl)

                table.setFixedPosition(i,25f,pageHeight-100f,pageWidth- 50f)

                table.setTextAlignment(TextAlignment.CENTER)

                table.addCell(Cell(1,1).add(image).setBorder(Border.NO_BORDER))
                table.addCell(Cell(2,1)
                    .add(Paragraph("Annal Gandhi Memorial Hospital Trichy MCH \n Report"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER)
                )
                table.addCell(Cell(3,1).add(image2).setBorder(Border.NO_BORDER))

                doc.add(table)

                val columnOfFromToDate = floatArrayOf(pageWidth/2,pageWidth/2)

                val fromToDateTable:Table= Table(columnOfFromToDate)

                fromToDateTable.setFixedPosition(i,25f,pageHeight-110f,pageWidth- 50f)

                fromToDateTable.addCell(Cell(1,1).add(Paragraph("From date :12-2-2010")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER))
                fromToDateTable.addCell(Cell(2,1).add(Paragraph("To date :10-2-2021")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER))
                doc.add(fromToDateTable)
            }
            doc.close()

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri: Uri = FileProvider.getUriForFile(
                this,
                this.packageName.toString() + ".provider",
                fileNew!!
            )

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/pdf")
            startActivity(intent)
        }

        private fun generatePDF() {
            // creating an object variable
            // for our PDF document.
            val pdfDocument = PdfDocument()

            //document




            // two variables for paint "paint" is used
            // for drawing shapes and we will use "title"
            // for adding text in our PDF file.
            val paint = Paint()
            val title = Paint()

            // we are adding page info to our PDF file
            // in which we will be passing our pageWidth,
            // pageHeight and number of pages and after that
            // we are calling it to create our PDF.
            val mypageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()

            // below line is used for setting
            // start page for our PDF file.
            val myPage = pdfDocument.startPage(mypageInfo)

            // creating a variable for canvas
            // from our page of PDF.
            val canvas: Canvas = myPage.canvas


            // below line is used to draw our image on our PDF file.
            // the first parameter of our drawbitmap method is
            // our bitmap
            // second parameter is position from left
            // third parameter is position from top and last
            // one is our variable for paint.
            canvas.drawBitmap(scaledbmp!!, 46f, 40f, paint)

    //        paint.setTextAlign(Paint.Align.RIGHT)
            canvas.drawBitmap(scaledbmp2!!, 620f, 40f, paint)

            // below line is used for adding typeface for
            // our text which we will be adding in our PDF file.
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

            // below line is used for setting text size
            // which we will be displaying in our PDF file.
            title.setTextSize(18f)

    //        val plain = Typeface.createFromAsset(this.assets, R.font.poppins)



            val customTypeface = ResourcesCompat.getFont(this,R.font.poppins)

    /*
            if your min Android API >= 26

            val customTypeface = resources.getFont(R.font.myfont)*/
            title.typeface = customTypeface;

            // below line is sued for setting color
            // of our text inside our PDF file.
            title.color = ContextCompat.getColor(this, R.color.black)
            title.isFakeBoldText = true

            // below line is used to draw text in our PDF file.
            // the first parameter is our text, second parameter
            // is position from start, third parameter is position from top
            // and then we are passing our variable of paint which is title.
            title.textAlign = Paint.Align.CENTER
            canvas.drawText("Annal Gandhi Memorial Hospital Trichy MCH",
                (canvas.width /2).toFloat(), 100f, title)

            title.textSize = 16f
            title.isFakeBoldText = false
            canvas.drawText("Patient Count Summary Report", (canvas.width /2).toFloat(), 140f, title)

            // similarly we are creating another text and in this
            // we are aligning this text to center of our PDF file.
            title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            title.color = ContextCompat.getColor(this, R.color.purple_200)
            title.textSize = 15f

            // below line is used for setting
            // our text to center of PDF.
            title.textAlign = Paint.Align.CENTER
            canvas.drawText("This is sample document which we have created.", 396f, 560f, title)

            // after adding all attributes to our
            // PDF file we will be finishing our page.
            pdfDocument.finishPage(myPage)

            // below line is used to set the name of
            // our PDF file and its path.
            val file = File(Environment.getExternalStorageDirectory(), "GFG.pdf")

            if (!file.exists()) {
                file.createNewFile();
            }
            try {
                // after creating a file name we will
                // write our PDF file to that location.
                pdfDocument.writeTo(FileOutputStream(file))
    /*
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val uri: Uri = FileProvider.getUriForFile(
                    this,
                    this.packageName.toString() + ".provider",
                    file!!
                )

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(uri, "application/pdf")
                startActivity(intent)*/

                // below line is to print toast message
                // on completion of PDF generation.
                Toast.makeText(
                    this,
                    "PDF file generated successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IOException) {
                // below line is used
                // to handle error
                e.printStackTrace()
            }
            // after storing our pdf to that
            // location we are closing our PDF file.
            pdfDocument.close()
        }


    }

