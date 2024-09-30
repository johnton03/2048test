import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        //设置标题
        this.setTitle("2048");
        //设置大小
        this.setSize(370,420);
        //设置界面置顶
        this.setAlwaysOnTop(true);
        //设置页面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式，进程退出
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置背影颜色
        this.getContentPane().setBackground(new Color(66,136,83));
        //设置窗体不允许变大
        this.setResizable(false);

    }
}
