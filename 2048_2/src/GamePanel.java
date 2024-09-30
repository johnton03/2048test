import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final int COLS = 4;
    private static final int ROWS = 4;
    private JFrame frame = null;
    private GamePanel panel = null;
    private Card[][] cards = new Card[COLS][ROWS];
    private String gameFlag = "start";

    public GamePanel(JFrame frame){
        this.setLayout(null);
        this.setOpaque(false);
        this.frame = frame;
        this.panel = this;

        createMenu(); //创建菜单

        initCard(); //创建卡片

        createRandomNum(); //随机创建一个卡片

        frame.addKeyListener(this);
        //createKeyListener(); //创建键盘监听
    }
    //创建键盘监听
    private void createKeyListener() {
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("111");
            }
        };
    }

    private void createRandomNum() {
        //随机好要显示的数字是2还是4
        int num;
        Random r = new Random();
        int n = r.nextInt(5) + 1; //随机取出1-5，1即为4，2-5即为2
        if(n == 1){
            num = 4;
        }else{
            num = 2;
        }

        //如果格子满了，则不需要再去取了
        if(cardIsFul()){
            return;
        }

        //取到卡片
        Card card = getRandomCard();
        //设置卡片数字
        if(card != null){
            card.setNum(num);
        }
    }

    private boolean cardIsFul() {
        Card card;
        for(int i = 0; i<ROWS; i++){
            for(int j = 0; j<COLS; j++){
                card = cards[i][j];
                if(card.getNum() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private Card getRandomCard() {
        Random r = new Random();
        int i = r.nextInt(ROWS);
        int j = r.nextInt(COLS);
        Card card = cards[i][j];

        if(card.getNum() == 0){
            return card;
        }
        //没找到就递归继续找
        return getRandomCard();
    }

    //创建卡片
    private void initCard() {
        Card card;
        for(int i = 0; i<ROWS; i++){
            for(int j = 0; j<COLS; j++){
                card = new Card(i, j);
                cards[i][j] = card;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制卡片
        drawCard(g);
    }
    //绘制卡片
    private void drawCard(Graphics g) {
        Card card;
        for(int i = 0; i<ROWS; i++){
            for(int j = 0; j<COLS; j++){
                card = cards[i][j];
                card.draw(g);
            }
        }
    }

    //创建字体方法
    private Font createFont(){
        return new Font("思源宋体", Font.BOLD, 18);
    }
    //创建菜单
    private void createMenu() {
        //创建字体
        Font font = createFont();
        //创建JMenuBar
        JMenuBar jmb = new JMenuBar();

        JMenu jm1 = new JMenu("游戏");
        jm1.setFont(font);
        //创建子项
        JMenuItem jmi1 = new JMenuItem("新游戏");
        jmi1.setFont(font);
        JMenuItem jmi2 = new JMenuItem("退出");
        jmi2.setFont(font);
        //添加子项
        jm1.add(jmi1);
        jm1.add(jmi2);

        JMenu jm2 = new JMenu("帮助");
        jm2.setFont(font);
        //创建子项
        JMenuItem jmi3 = new JMenuItem("操作帮助");
        jmi3.setFont(font);
        JMenuItem jmi4 = new JMenuItem("胜利条件");
        jmi4.setFont(font);
        //添加子项
        jm2.add(jmi3);
        jm2.add(jmi4);

        jmb.add(jm1);
        jmb.add(jm2);

        frame.setJMenuBar(jmb);

        //添加事件监听
        jmi1.addActionListener(this);
        jmi2.addActionListener(this);
        jmi3.addActionListener(this);
        jmi4.addActionListener(this);

        //设置指令
        jmi1.setActionCommand("restart");
        jmi2.setActionCommand("exit");
        jmi3.setActionCommand("help");
        jmi4.setActionCommand("win");
    }

    //事件监听（点击菜单）
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("restart")){
            System.out.println("新游戏");
            restart();
        }else if(command.equals("exit")){
            System.out.println("退出游戏");
            Object[] options = {"退出","取消"};
            int res = JOptionPane.showOptionDialog(this,"你确定要退出游戏吗",
                    "",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,
                    options,options[0]);
            if(res == 0){ //确认退出
                System.exit(0);
            }

        }else if(command.equals("help")){
            System.out.println("帮助");
        }else if(command.equals("win")){
            System.out.println("胜利条件");
        }
    }

    private void restart() {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("111");
         if(!"start".equals(gameFlag)){
             return;
         }
         int key = e.getKeyCode();
         switch (key){
             //向上
             case KeyEvent.VK_UP:
             case KeyEvent.VK_W:
                 moveCard(1);
                 break;
             //向右
             case KeyEvent.VK_RIGHT:
             case KeyEvent.VK_D:
                 moveCard(2);
                 break;
             //向下
             case KeyEvent.VK_DOWN:
             case KeyEvent.VK_S:
                 moveCard(3);
                 break;
             //向左
             case KeyEvent.VK_LEFT:
             case KeyEvent.VK_A:
                 moveCard(4);
                 break;
             default:
                 break;
         }
    }

    private void moveCard(int dir) {
        //清理卡片的合并状态
        clearCard();
        //判断移动方向
        if(dir ==1 )
            moveCardTop(true);
        else if(dir == 2)
            moveCardRight(true);
        else if(dir == 3)
            moveCardButtom(true);
        else if(dir == 4)
            moveCardLeft(true);

        //创建新卡片
        createRandomNum();

        //重绘
        repaint();

        //判断游戏是否结束
        gameOverOrNot();
    }

    private void gameOverOrNot() {
        /**
         * 结束条件：
         *  1、位置已满
         *  2、4个方向都没有可以合并的卡片
         */
        if (isWin()){ //胜利
            gameWin();
        }else if(cardIsFul()){ //位置已满
            if(moveCardTop(false)||
                  moveCardRight(false)||
                  moveCardButtom(false)||
                  moveCardLeft(false)){
                return;
            }else{
                gameOver();
            }
        }
    }

    private void gameOver() {
        gameFlag = "end";
        //弹出结果提示
        UIManager.put("OptionPane.buttonFont", new FontUIResource((new Font("思源宋体",Font.ITALIC,18))));
        UIManager.put("OptionPane.messageFont", new FontUIResource((new Font("思源宋体",Font.ITALIC,18))));
        JOptionPane.showMessageDialog(frame,"你失败了，请再接再厉!");
    }

    private void gameWin() {
        gameFlag = "end";
        //弹出结果提示
        UIManager.put("OptionPane.buttonFont", new FontUIResource((new Font("思源宋体",Font.ITALIC,18))));
        UIManager.put("OptionPane.messageFont", new FontUIResource((new Font("思源宋体",Font.ITALIC,18))));
        JOptionPane.showMessageDialog(frame,"你成功了，太棒了！");
    }

    private boolean isWin() {
        Card card;
        for(int i = 0; i<ROWS; i++){
            for(int j = 0; j<COLS; j++){
                card = cards[i][j];
                if(card.getNum() == 2048){ //赢了
                    return true;
                }
            }
        }
        return false;
    }

    private void clearCard() {
        for(int i = 0; i<ROWS; i++){
            for(int j = 0; j<COLS; j++){
                cards[i][j].setMerge(false);
            }
        }
    }

    private boolean moveCardTop(boolean b) {
        //System.out.println("move top");
        boolean res=false;
        Card card;
        for(int i = 1; i<ROWS; i++){
            for(int j = 0; j<COLS; j++){
                card = cards[i][j];
                if(card.getNum() != 0){ //只要卡片不是空白 就要移动
                    if(card.moveTop(cards,b)){
                        res=true;
                    }
                }
            }
        }
        return res;
    }

    private boolean moveCardRight(boolean b) {
        boolean res=false;
        Card card;
        for(int i = 0; i<ROWS; i++){
            for(int j = COLS-2; j>=0; j--){
                card = cards[i][j];
                if(card.getNum() != 0){ //只要卡片不是空白 就要移动
                    if(card.moveRight(cards,b)){
                        res=true;
                    }
                }
            }
        }
        return res;
    }

    private boolean moveCardButtom(boolean b) {
        boolean res=false;
        Card card;
        for(int i = ROWS-2; i>=0; i--){
            for(int j = 0; j<COLS; j++){
                card = cards[i][j];
                if(card.getNum() != 0){ //只要卡片不是空白 就要移动
                    if(card.moveButtom(cards,b)){
                        res=true;
                    }
                }
            }
        }
        return res;
    }

    private boolean moveCardLeft(boolean b) {
        boolean res=false;
        Card card;
        for(int i = 0; i<ROWS; i++){
            for(int j = 1; j<COLS; j++){
                card = cards[i][j];
                if(card.getNum() != 0){ //只要卡片不是空白 就要移动
                    if(card.moveLeft(cards,b)){
                        res=true;
                    }
                }
            }
        }
        return res;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
