import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;  

//Class to process image, create and assign clusters
public class ImageDisplay {
    //Variable declarations
    //Number of Clusters N - Argument 2
    static int N;
    //Size of Vector M - Argument 1
    static int M;
    //Image File Name - Argument 0
    static String fileName;
    //Instance of Clusters
    static Cluster[] clusters;
    //Checking if image is rgb file
    static boolean isRGB = false;

    //Create an instance of JFrame
    //Instance of JFrame class as Output Display Frame
    JFrame frame;

    //Labels to show labels of left side and the right side images
    JLabel lbIm1;
    JLabel lbIm2;

    //Height and width of image
    int width = 352;
    int height = 288;

    //Creating new empty BufferedImages to represent original and quantized image
    static BufferedImage img1;
    static BufferedImage img2;


    //Strings to hold label texts based on the program that is executed
    String labelText1;
    String labelText2;

    //Function to draw lines
    public void drawLine(BufferedImage image, double x1, double y1, double x2, double y2) 
    {
        //Graphics2D object
        Graphics2D g = image.createGraphics();
        //set color of line to black
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        //draw lines fro x1,y1 to x2,y2
        g.drawLine((int) Math.ceil(x1), (int) Math.ceil(y1), (int) Math.ceil(x2), (int) Math.ceil(y2));
        //add the line onto the image passed as the argument
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
    
    //Create N clusters and assign N pixels to N clusters
    public void createCulsters(BufferedImage image){
        //create N clusters
        clusters = new Cluster[N];
        //declare x and y coordinates
        int x = 0;
        int y = 0;
        //dx and dy are the amount by which the x and y coordinates are updated for each cluster
        int dx = width/N;
        int dy = height/N;
        //if N is greater than height or width dx and dy are 0, in such a case assign dx and dy to 2
        if(dx==0)
            dx=2;
        if(dy==0)
            dy=2;
        //Create N clusters and assign N pixels to N clusters
        for (int i = 0; i < N; i++) {
            //Assign clusters to a given location on image
            clusters[i] = new Cluster(i,image.getRGB(x,y), isRGB);
            //Update x and y coordinates
            x = x + (dx);
            y = y + (dy);
            //Update x and y so that they dont exceed the height and width of the image
            x = x%352;
            y=y%288;
        }
    }

   
    //Finding nearest cluster to an image
    public Cluster nearestCluster(int pixel){
        //Declare a cluster
        Cluster cluster = null;
        //min value fro finding min distance
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) 
        {
            //Find distance of pixel from each cluster
            int distance = clusters[i].dist(pixel);
            //If distance is less than min update min
            if (min > distance){
                min = distance;
                //Assign pixel to that cluster
                cluster = clusters[i];
            }
        }
        return cluster;
    }

    

        public void ProcessImage()
        {
        
        //Initialize a plain white images for original and quantized image
        img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Set background color for the images
        for (int y = 0; y < img1.getHeight(); y++) 
        {
            for (int x = 0; x < img1.getWidth(); x++) 
            {
                byte r = (byte) 255;
                byte g = (byte) 255;
                byte b = (byte) 255;
                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8)| (b & 0xff);
                //set pixels to original image
                img1.setRGB(x, y, pix);
            }
        }

       for (int y = 0; y < img2.getHeight(); y++) 
       {
            for (int x = 0; x < img2.getWidth(); x++) 
            {
                //bytes representing R, G, B channels
                byte r = (byte) 255;
                byte g = (byte) 255;
                byte b = (byte) 255;
                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8)| (b & 0xff);
                //set pixels to quantized image
                img2.setRGB(x, y, pix);
            }
        }


        //Draw Borders for the original image
        drawLine(img1, 0, 0, img1.getWidth() - 1, 0);                
        drawLine(img1, 0, img1.getHeight() - 1, img1.getWidth() - 1, img1.getHeight() - 1);   
        drawLine(img1, 0, 0, 0, img1.getHeight() - 1);               
        drawLine(img1, img1.getWidth() - 1, img1.getHeight() - 1, img1.getWidth() - 1, 0);
        
        //Draw Borders for the quantized image
        drawLine(img2, 0, 0, img2.getWidth() - 1, 0);                
        drawLine(img2, 0, img2.getHeight() - 1, img2.getWidth() - 1, img2.getHeight() - 1);   
        drawLine(img1, 0, 0, 0, img2.getHeight() - 1);               
        drawLine(img1, img2.getWidth() - 1, img2.getHeight() - 1, img2.getWidth() - 1, 0);

        //Checking if the file is .rgb file type
        if (fileName.endsWith(".rgb")){
            isRGB = true;
        }

        try {
            int offset = 0;
            int numr;
            int index = 0;
            //Input file for image
            //value received from the command line
            File imageFile = new File(fileName);
            //create insputstream object
            InputStream fileStream = new FileInputStream(imageFile);
            //length of the file
            long len = imageFile.length();
            //Array of bytes
            byte[] bytes = new byte[((int)(10*len))];
            //Update offset values
            while (offset < bytes.length && (numr=fileStream.read(bytes, offset, bytes.length-offset)) >= 0){
                offset = offset + numr;
            }
            //Read the image file
            for (int y = 0; y < height; y++) 
            {
                for (int x = 0; x < width; x++) 
                {
                    //bytes representing R, G, B channels
                    byte r = bytes[index];
                    byte g = bytes[index];
                    byte b = bytes[index];
                    int pixel = 0Xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff) << 0;
                    //set pixels to values read from the .rgb file
                    img1.setRGB(x,y,pixel);
                    index = index + 1;
                }
            }
            //Close the image file
            fileStream.close();
        }catch (Exception exception) {
            //show any error that occurs
            exception.printStackTrace();
        }

        //Computer vector space as defined in the assignment question
        int[] vectorSpace = determineVectorSpace(img1);

        //Create the new image using the vector space mapping
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int clusterid = vectorSpace[width * y + x];
                img2.setRGB(x, y, clusters[clusterid].pixget());
            }
        }
    }
   

    public void showIms() {
        //Initialize instance of JFrame
        frame = new JFrame();
        //Create instance of GridBagLayout
        GridBagLayout gLayout = new GridBagLayout();
        //Set frame layout
        frame.getContentPane().setLayout(gLayout);

        //Create an instance of JLabel for the original image
        JLabel lbText1 = new JLabel();
        //Set title for the original image
        lbText1.setText(this.labelText1);
        //Set alignment for original image
        lbText1.setHorizontalAlignment(SwingConstants.CENTER);
        //Create an instance of JLabel for the Quantized image
        JLabel lbText2 = new JLabel();
        //Set title for the Quantized image
        lbText2.setText(labelText2);
        //Set alignment for Quantized image
        lbText2.setHorizontalAlignment(SwingConstants.CENTER);
        //Initialize JLabel object with the original image
        lbIm1 = new JLabel(new ImageIcon(img1));
        //Initialize JLabel object with the Quantized image
        lbIm2 = new JLabel(new ImageIcon(img2));

        //Displaying the original and quantized image in a grid format
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        //Add text for the original image to the left side of the frame
        frame.getContentPane().add(lbText1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        //Add text for the Quantized image to the right side of the frame
        frame.getContentPane().add(lbText2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        //Add the original image to the left side of the frame
        frame.getContentPane().add(lbIm1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        //Add the Quantized image to the right side of the frame
        frame.getContentPane().add(lbIm2, c);

        System.out.println("Original and Quantized Image are");
        frame.pack();
        frame.setVisible(true);
        //Exit on Close is set 
        //When the popup window is closed the program exitz and releasez all memory that was allocated
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public int[] determineVectorSpace(BufferedImage image){
        //Boolean refine varibale
        boolean refine = true;
        //Call the create clusters function to create N random clusters
        createCulsters(image);
        //Create a vector space array variable with length as number of pixels
        int[] vectorSpace = new int[width*height];
        //Initialize the vector space with -1
        Arrays.fill(vectorSpace,-1);
        //Run the loop till the refine boolean variable is true
        while(refine){
            refine = false;
            for (int y = 0; y < height; y++) 
            {
                for (int x = 0; x < width; x++) 
                {   
                        //Get pixel at location x, y
                        int pixel = image.getRGB(x, y);
                        //Call nearestCluster function to find the nearest cluster to tye given pixel
                        Cluster cluster = nearestCluster(pixel);
                        //If cluster id not equals to vector spcae at width*y+x
                        if (cluster.getId() != vectorSpace[width * y + x]) 
                        {
                            if (-1 != vectorSpace[width * y + x]) 
                            {
                                //Remove pixel
                                clusters[vectorSpace[width * y + x]].pixdel(pixel, isRGB);
                            }
                            //Add pixel to the cluster
                            cluster.pixadd(pixel, isRGB);
                            //set refine to true
                            refine = true;
                            //update vector space
                            vectorSpace[width * y + x] = cluster.getId();
                        }   
                }
            }
        }
        //Return the modified Vector Space
        return vectorSpace;
    }

    //Check if N is a power of 2
    private static boolean isPowerOfTwo(int n)
    {
        //0 is not a power of 2
        if (n == 0) {
            return false;
        }
        while (n != 1)
        {   
            //Odd numbers cant be power of 2
            if (n % 2 != 0) {
                return false;
            }
            //Update n
            n = n / 2;
        }
        //If n is a power of 2 while loop ends without returing false
        return true;
    }

    //Main function
    //Command line arguments passed
    public static void main(String[] args) {
        //Initialize the image display class used in Assignment 1
        ImageDisplay imgDisplay = new ImageDisplay();

        //Perform Vector Quantization
        
        //Argument 2 is N
        int N1 = Integer.parseInt(args[2]);
        //N can only be a power of 2
        //N can only be positive
        //Stop Execution in all other cases.
        if (N1 <= 0){
            System.out.println("N can't be negative, invalid number of clusters");
            System.exit(1);
        }else if (!isPowerOfTwo(N1)){
            System.out.println("Number of clusters is not in powers of 2,  invalid number of clusters");
            System.exit(1);
        }
        //Assign N
        ImageDisplay.N = N1;

        //FileName of the original Image is Argument 0 of command line argument
        String imageName = args[0];
        //If the name is empty stop execution
        //Check if file is a raw or rgb binary file type
        //If not stop execution
        if (imageName != null){
            if (imageName.length() == 0){
                System.out.println("Invalid file name");
                System.exit(1);
            }else if (imageName.endsWith(".raw") || imageName.endsWith(".rgb")){
                ImageDisplay.fileName  = imageName;
            }
            else{
                System.out.println("Invalid file name");
                System.exit(1);
            }
        }else{
            System.out.println("Invalid file name");
                System.exit(1);
        }

        //M is argument 1
        //M is 2
        int m = Integer.parseInt(args[1]);
        if (m==2){
            ImageDisplay.M = m;
        }else{
            System.out.println("M not equals 2. Quit");
            System.exit(1);
        }
        

        //Print program parameters
        System.out.printf("***Parameters Used are:***\n");
        System.out.printf("Image File Name: %s\n",ImageDisplay.fileName);
        
        System.out.printf("Number of Clusters: %d\n",ImageDisplay.N);
        System.out.printf("Size of Vector: %d\n",ImageDisplay.M);
        if (ImageDisplay.fileName.endsWith(".rgb")){
            System.out.printf("Image Type: RGB File-Grayscale Image\n");
        }
        System.out.println("\nImage Quantization Results displayed in window...");
        //Call Process Image function
        imgDisplay.ProcessImage();
        //At the end show both the original and quantized image
        imgDisplay.showIms();
    }
}