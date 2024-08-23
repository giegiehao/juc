package org.example.t4;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

@Slf4j(topic = "c.test")
public class ProducerConsumerMode {
    static MessageQueue queue = new MessageQueue(5);
    public static void main(String[] args) throws InterruptedException {
        //生产者
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            sleep(100);
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    try {
                        sleep(500);
                        log.debug("发送消息");
                        queue.put(new Message(j - finalI, "消息"));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        sleep(300);
        //消费者
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        Message take = queue.take();
                        log.debug("收到消息{}", take);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}

@Slf4j(topic = "c.queue")
class MessageQueue {
    private final LinkedList<Message> queue = new LinkedList<>();
    private final int capacity; //队列大小

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        LockSupport.park();
    }

    //存
    public void put(Message message) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() == capacity) {
                log.debug("队列满了，等一下");
                queue.wait(); //满队列 等待
            }
            queue.addLast(message);
            queue.notifyAll(); //在空队列的情况下唤醒一下等待的消费者。
        }
    }
    //取
    public Message take() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                log.debug("队列空了，等一下");
                queue.wait(); //等待消息
            }
            Message meg = queue.removeFirst();
            queue.notifyAll(); //在满队列的情况下唤醒等待存放的生产者。
            return meg;
        }
    }
}

@Data
class Message {
    int id;
    String meg;
    public Message(int id, String meg) {
        this.id = id;
        this.meg = meg;
    }
}
