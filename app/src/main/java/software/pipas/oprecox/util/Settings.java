package software.pipas.oprecox.util;

public abstract class Settings
{
    private static boolean locked;

    public static void setLocked(boolean l)
    {
        locked = l;
    }

    public static boolean isLocked()
    {
        return locked;
    }
}
