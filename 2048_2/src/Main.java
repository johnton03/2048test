public class Main {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        GamePanel panel = new GamePanel(frame);
        frame.add(panel);
        //让界面显示出来，放在最后
        //如果是false，那么就不会有窗口出现
        frame.setVisible(true);
    }
}
