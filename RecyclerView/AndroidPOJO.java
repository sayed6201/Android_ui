
public class AndroidPOJO {
    String version = "", name = "", release = "";

    public AndroidPOJO(String version, String name, String release) {
        this.version = version;
        this.name = name;
        this.release = release;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getRelease() {
        return release;
    }
}
