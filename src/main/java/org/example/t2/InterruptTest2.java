package org.example.t2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test")
public class InterruptTest2 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();
        Thread.sleep(5000);
        twoPhaseTermination.stop();
    }
}

@Slf4j(topic = "c.test")
class TwoPhaseTermination {
    private Thread listening;

    public void start() {
        this.listening = new Thread(() -> {
            while(true) {
                Thread thread = Thread.currentThread();
                if (thread.isInterrupted()) {
                    log.debug("数据处理");
                    break;
                }
                try {
                    thread.sleep(1000);
                    log.debug("正在监听");
                } catch (InterruptedException e) {
                    log.debug("收到中断信号");
                    //确定是否终端
                    thread.interrupt();
                }
            }
        });
        listening.start();
    }

    public void stop() {
        listening.interrupt();
    }
}
