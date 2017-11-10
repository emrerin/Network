//package ad1.ss16.pa;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 * Simplified SecurityManager in the ADS1 Framework.
 * <p>
 * <b>IMPORTANT:</b> Do not change this class. All changes made to this class
 * are removed after the submission of the solution. Thereby, the submitted solution
 * can fail during the tests.
 * </p>
 */
public class ADS1SecurityManager extends SecurityManager {
    /**
     * Create new SecurityManager
     */
    public ADS1SecurityManager() {
        super();
    }

    @Override
    public void checkAccept(String arg0, int arg1) {
        checkOrigin("ServerSocket.accept()");
    }

    @Override
    public void checkAccess(Thread arg0) {
        checkOrigin("Thread access");
    }

    @Override
    public void checkAccess(ThreadGroup arg0) {
        checkOrigin("ThreadGroup access");
    }

    @Override
    public void checkConnect(String arg0, int arg1) {
        checkOrigin("Socket.connect()");
    }

    @Override
    public void checkConnect(String arg0, int arg1, Object arg2) {
        checkOrigin("Socket.connect()");
    }

    @Override
    public void checkCreateClassLoader() {
        checkOrigin("Create classLoader");
    }

    @Override
    public void checkDelete(String arg0) {
        checkOrigin("File.delete()");
    }

    @Override
    public void checkExec(String arg0) {
        checkOrigin("Runtime.exec()");
    }

    @Override
    public void checkExit(int status) {
        checkOrigin("System.exit()");
    }

    @Override
    public void checkLink(String arg0) {
        checkOrigin("Runtime.load()/loadLibrary()");
    }

    @Override
    public void checkListen(int arg0) {
        checkOrigin("Listen to port");
    }

    @Override
    public void checkMulticast(InetAddress arg0) {
        checkOrigin("Multicast");
    }

    @SuppressWarnings("rawtypes")
    private void checkOrigin(String type) throws SecurityException {
        for (Class x : this.getClassContext()) {
            if (x == Network.class) {
                throw new SecurityException(type + ": " + x);
            } else if (x == Tester.class) {
                return;
            }
        }
        throw new SecurityException(type);
    }

    @Override
    public void checkPermission(Permission perm) {
        checkOrigin("Permission Access");
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        checkOrigin("Permission Access");
    }

    @Override
    public void checkPrintJobAccess() {
        checkOrigin("Print");
    }

    @Override
    public void checkPropertiesAccess() {
        checkOrigin("System.set/getProperties()");
    }

    @Override
    public void checkPropertyAccess(String arg0) {
        checkOrigin("System.getProperty(" + arg0 + ")");
    }

    @Override
    public void checkRead(FileDescriptor arg0) {
    }

    @Override
    public void checkRead(String arg0) {
    }

    @Override
    public void checkRead(String arg0, Object arg1) {
    }

    @Override
    public void checkSecurityAccess(String target) {
        checkOrigin("Security Access");
    }

    @Override
    public void checkSetFactory() {
        checkOrigin("Set Socket/ServerSocket/URL Factory");
    }

    @Override
    public void checkWrite(FileDescriptor arg0) {
        checkOrigin("Write file descriptor");
    }

    @Override
    public void checkWrite(String arg0) {
        checkOrigin("Write file");
    }
}
