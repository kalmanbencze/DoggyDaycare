package me.kalmanbncz.doggydaycare.domain.dog;

import static me.kalmanbncz.doggydaycare.domain.dog.Status.ERROR;
import static me.kalmanbncz.doggydaycare.domain.dog.Status.LOADING;
import static me.kalmanbncz.doggydaycare.domain.dog.Status.SUCCESS;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
class Resource<T> {

    private final Status status;

    private final T data;

    private final String message;

    public Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource success(T data) {
        return new Resource<T>(SUCCESS, data, null);
    }

    public static <T> Resource error(String msg, T data) {
        return new Resource<T>(ERROR, data, msg);
    }

    public static <T> Resource loading(T data) {
        return new Resource<T>(LOADING, data, null);
    }
}
