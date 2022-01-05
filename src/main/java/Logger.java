public class Logger
{
    static int logLevel = 0;

    Class parentClass = null;

    public Logger(Class parentClass)
    {
        this.parentClass = parentClass;
    }

    public static int getLogLevel()
    {
        return logLevel;
    }

    public static void setLogLevel(int logLevel)
    {
        Logger.logLevel = logLevel;
    }

    public void logMessage(String message)
    {
        if(logLevel >= 1)
        {
            System.out.println(parentClass.getCanonicalName() + " : MESSAGE:\n" + message + "\n===========");
        }
    };

    public void logWarning(String message)
    {
        if(logLevel >= 3)
        {
            System.out.println(parentClass.getCanonicalName() + " : WARNING:\n" + message + "\n===========");
        }
    };

    public void logError(String message)
    {
        if(logLevel >= 2)
        {
            System.out.println(parentClass.getCanonicalName() + " : ERROR:\n" + message + "\n===========");
        }
    };

    public void logDebug(String message)
    {
        if (logLevel >= 4)
        {
            System.out.println(parentClass.getCanonicalName() + " : DEBUG:\n" + message + "\n===========");
        }
    };
}
