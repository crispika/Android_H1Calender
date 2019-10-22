package com.comp90018.H1Calendar.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Main stream to send and receive event
 * The bus to send/receive events
 */

public class EventBus {
    public static EventBus instance;

    //使subject线程安全
    private static final Subject<Object, Object> eventBus = new SerializedSubject<>(PublishSubject.create());

    public static EventBus getInstance(){
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    //发送event
    public static void send(Object object){eventBus.onNext(object);}
    //得到subject
    public Observable<Object> getSubject(){return eventBus;}
}
