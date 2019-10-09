package com.comp90018.H1Calendar.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 总线，收发event
 * The bus to send/receive events
 */

public class EventBus {
    public static EventBus instance;

    private final Subject<Object, Object> eventBus = new SerializedSubject<>(PublishSubject.create());

    public EventBus getInstance(){
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    //发送event
    public void send(Object object){eventBus.onNext(object);}
    //得到subject
    public Observable<Object> getSubject(){return eventBus;}
}
