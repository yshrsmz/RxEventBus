package net.yslibrary.rxeventbus;

import org.junit.Before;
import org.junit.Test;

import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yshrsmz on 2016/07/01.
 */
public class EventBusTest {
    EventBus bus;

    @Before
    public void setup() {
        bus = new EventBus();
    }

    @Test
    public void testSubscribe() {
        TestSubscriber<TestA> subscriber = new TestSubscriber<>();

        bus.on(TestA.class)
                .subscribe(subscriber);

        bus.emit(new TestA(99));

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        assertThat(subscriber.getOnNextEvents().get(0).id).isEqualTo(99);
    }

    @Test
    public void testSubscribeDifferentEvent() {
        TestSubscriber<TestA> subscriber = new TestSubscriber<>();

        bus.on(TestA.class)
                .subscribe(subscriber);

        bus.emit(new TestB(99));

        subscriber.assertNoErrors();
        subscriber.assertNoValues();

        bus.emit(new TestA(1));

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        assertThat(subscriber.getOnNextEvents().get(0).id).isEqualTo(1);
    }

    @Test
    public void testDistinctSameClassDifferentContent() {
        TestSubscriber<TestC> subscriber = new TestSubscriber<>();

        bus.on(TestC.class)
                .distinctUntilChanged(new Func1<TestC, Integer>() {
                    @Override
                    public Integer call(TestC testC) {
                        return testC.id;
                    }
                })
                .subscribe(subscriber);

        bus.emit(new TestC(1));
        bus.emit(new TestC(1));
        bus.emit(new TestA(1));

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);

        bus.emit(new TestC(2));

        subscriber.assertNoErrors();
        subscriber.assertValueCount(2);

    }


    class TestA implements EventBus.Event {
        public final int id;

        public TestA(int id) {
            this.id = id;
        }
    }

    class TestB implements EventBus.Event {
        public final int id;

        public TestB(int id) {
            this.id = id;
        }
    }

    class TestC implements EventBus.Event {
        public final int id;

        public TestC(int id) {
            this.id = id;
        }
    }
}
