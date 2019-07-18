package xiake.vscreenshot.img;

import java.io.Serializable;

/**
 * @author: zzp
 * @since: 2015-06-16
 */
public class SingleImageModel implements Serializable {
    public String path;
    public boolean isPicked;
    public long date;
    public long id;
    public SingleImageModel(String path, boolean isPicked, long date, long id){
        this.path = path;
        this.isPicked = isPicked;
        this.date = date;
        this.id = id;
    }
    public SingleImageModel(){

    }
    public boolean isThisImage(String path){
        return this.path.equalsIgnoreCase(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
