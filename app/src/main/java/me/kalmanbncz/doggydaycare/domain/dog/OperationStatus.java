package me.kalmanbncz.doggydaycare.domain.dog;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
public class OperationStatus {

    public static final int LOCAL_SUCCESS = 1;

    public static final int ONLINE_SUCCESS = 2;

    public static final int FAILED = -1;

    public static final int STARTED = 0;

    private final int status;

    public OperationStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return status == LOCAL_SUCCESS;
    }
}
