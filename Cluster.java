import java.io.File;

//Class that encapsulates each cluster and give functions to add and remove pixels and create mapping for quantized image
public class Cluster {
    //Parameters
    
    public int id;

    //Return the id of a given cluster since Id is a private variable
    int getId(){
        return id;
    }
    
    public Cluster(int id, int pixel, boolean isRGB)
    {
        //Set ID of cluster
        this.id = id;
        //Set r channel
        avgr = (pixel >> 16) & 0xff;
        //Set g and b channel
        if (isRGB) {
            avgg = (pixel >> 8) & 0xff;
            avgb = (pixel & 0xff);
        }else{
            avgg = avgr;
            avgb = avgr;
        }
        //CAlculate avg pixel value
        avg = 0Xff000000 | ((avgr & 0xff) << 16) | ((avgg & 0xff) << 8) | (avgb & 0xff) << 0;
        //Assign value to pixel
        pixadd(pixel, isRGB);
    }
    
    int avg, avgr, avgg,avgb, totr, totg, totb, pixno;

    //Get pixel value for a given pl=ixel
    int pixget(){
        //Average red value
        int r = avgr;
        //Average green value
        int g = avgg;
        //Average blue value
        int b = avgb;
        //return the pixel value
        return 0Xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    

    //Method to add new pixels and compute the neighbouring value avgs
    void pixadd(int pixel, boolean isRGB){
        //Extract red component
        int r = (pixel >> 16) & 0xff;
        int g, b;
        //Extract green component
        //Extract blue component
        if (isRGB) {
            g = (pixel >> 8) & 0xff;
            b = (pixel & 0xff);
        }else{
            g = r;
            b = r;
        }
        //update pixek=l numbers
        pixno = pixno + 1;
        //Update total red pixels
        totr = totr + r;
        //Update total green pixels
        totg = totg + g;
        //Update total vlue pixels
        totb = totb + b;
        //Update average red pixels
        avgr = totr/pixno;
        //Update average blue pixels
        avgb = totb/pixno;
        //Update average green pixels
        avgg = totg/pixno;
        
    }

    

    //Calculate distance betwen two given pixels
    int dist(int pixel){
        int px = pixel & 0xFFFFFFFF;
        //Return distance
        return Math.abs(avg - px);
    }
    
    //Remove new pixels
    //Recompute the neighbouring value avgs
    void pixdel(int pixel, boolean isRGB){
        //Extract r component
        int r = (pixel >> 16) & 0xff;
        int g, b;
        //Get the g and b components
        if (isRGB) {
            g = (pixel >> 8) & 0xff;
            b = (pixel & 0xff);
        }else{
            g = r;
            b = r;
        }
        //Update pixel numbers
        pixno = pixno - 1;
        //Update r component
        totr = totr - r;
        //Update g component
        totg = totg - g;
        //Update b component
        totb = totb - b;
        //Find avg r component
        avgr = totr/pixno;
        //Find avg g component
        avgg = totg/pixno;
        //Find avg b component
        avgb = totb/pixno;
    }
}
