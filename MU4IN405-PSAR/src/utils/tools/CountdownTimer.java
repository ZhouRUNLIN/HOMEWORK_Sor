package utils.tools;

public class CountdownTimer {
    private int timeInSeconds;

    public CountdownTimer(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public void start() {
        int totalSteps = 100;  // 总的步数为100，对应于百分比
        int delay = timeInSeconds * 10;  // 总延时为倒计时时间乘以10毫秒

        try {
            for (int i = 0; i <= totalSteps; i++) {
                System.out.print("\r");
                System.out.print("进度: " + i + "% " + progressBar(i, totalSteps));
                Thread.sleep(delay);  // 短暂延时以快速更新显示
            }
            System.out.print("\r");
            System.out.println("倒计时结束!          ");
        } catch (InterruptedException e) {
            System.err.println("倒计时被中断!");
            Thread.currentThread().interrupt();
        }
    }

    private String progressBar(int current, int total) {
        int percentage = (int) ((double) current / total * 50);  // 计算进度条长度
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 50; i++) {
            if (i < percentage) {
                bar.append("=");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");
        return bar.toString();
    }

//    public static void main(String[] args) {
//        CountdownTimer timer = new CountdownTimer(5);  // 创建一个5秒的倒计时
//        timer.start();
//    }
}
