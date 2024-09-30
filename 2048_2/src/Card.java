import java.awt.*;

public class Card {

    private int x = 0; //x坐标
    private int y = 0; //y坐标
    private int w = 80; //宽
    private int h = 80; //高
    private int i = 0; //下标i
    private int j = 0; //下标j

    private int start = 10; //偏移量
    private int num = 0; //中间的数值
    private boolean merge = false; //是否合并

    public Card(int i, int j){
        this.i = i;
        this.j = j;

        cal();
    }
    //计算坐标
    private  void cal(){
        this.x = start + j*w + (j+1)*5;
        this.y = start + i*h + (i+1)*5;
    }
    //得到该字符串的长度
    public static int getWordWidth(Font font, String content, Graphics g){
        FontMetrics metrics = g.getFontMetrics(font);
        int width = 0;
        for(int i = 0; i < content.length(); i++){
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }
    //卡片的绘制
    public void draw(Graphics g) {
        //根据数字获取对应颜色
        Color color = getColor();
        Color oldColor = g.getColor();

        g.setColor(color); //设置新颜色
        g.fillRoundRect(x,y,w,h,4,4);
        //绘制数字
        if(num != 0){
            g.setColor(new Color(125,78,51));
            Font font = new Font("思源宋体",Font.BOLD,30);
            g.setFont(font);
            String text = num + "";
            int tx = x + (w - getWordWidth(font,text,g)) / 2;
            int ty = y + 50;
            g.drawString(text,tx,ty);
        }
        g.setColor(oldColor); //还原颜色

    }

    //获取颜色
    private Color getColor(){
        Color color = null;
        switch (num){
            case 2:
                color = new Color(238,244,234);
                break;
            case 4:
                color = new Color(222,236,200);
                break;
            case 8:
                color = new Color(174,213,130);
                break;
            case 16:
                color = new Color(142,201,75);
                break;
            case 32:
                color = new Color(111,148,48);
                break;
            case 64:
                color = new Color(76,174,124);
                break;
            case 128:
                color = new Color(60,180,144);
                break;
            case 256:
                color = new Color(45,130,120);
                break;
            case 512:
                color = new Color(9,97,26);
                break;
            case 1024:
                color = new Color(242,177,121);
                break;
            case 2048:
                color = new Color(223,185,0);
                break;
            default:
                color = new Color(92,151,117);
                break;
        }
        return color;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }
    //向上移动的方法
    public boolean moveTop(Card[][] cards,boolean b) {
        //设定递归的退出条件
        if(i == 0){
            return false;
        }
        //上一个卡片
        Card prev = cards[i-1][j];
        if(prev.getNum() == 0){ //交换的
            if(b){
                prev.setNum(this.num);
                this.num = 0;
                prev.moveRight(cards,b);
            }
            return true; //表示可以移动
        }else if(prev.getNum() == this.num && !prev.merge){ //合并
            if(b){
                prev.merge = true;
                prev.setNum(2 * this.num);
                this.num = 0;
            }
            return true;
        }else{
            return false;
        }
    }

    public void setMerge(boolean b) {
        this.merge = b;
    }

    public boolean moveRight(Card[][] cards,boolean b) {
        //设定递归的退出条件
        if(j == 3){
            return false;
        }
        //上一个卡片
        Card prev = cards[i][j+1];
        if(prev.getNum() == 0){ //交换的
            if(b){
                prev.setNum(this.num);
                this.num = 0;
                prev.moveRight(cards,b);
            }
            return true; //表示可以移动
        }else if(prev.getNum() == this.num && !prev.merge){ //合并
            if(b){
                prev.merge = true;
                prev.setNum(2 * this.num);
                this.num = 0;
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean moveButtom(Card[][] cards,boolean b) {
        //设定递归的退出条件
        if(i == 3){
            return false;
        }
        //上一个卡片
        Card prev = cards[i+1][j];
        if(prev.getNum() == 0){ //交换的
            if(b){
                prev.setNum(this.num);
                this.num = 0;
                prev.moveRight(cards,b);
            }
            return true; //表示可以移动
        }else if(prev.getNum() == this.num && !prev.merge){ //合并
            if(b){
                prev.merge = true;
                prev.setNum(2 * this.num);
                this.num = 0;
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean moveLeft(Card[][] cards,boolean b) {
        //设定递归的退出条件
        if(j == 0){
            return false;
        }
        //上一个卡片
        Card prev = cards[i][j-1];
        if(prev.getNum() == 0){ //交换的
            if(b){
                prev.setNum(this.num);
                this.num = 0;
                prev.moveRight(cards,b);
            }
            return true; //表示可以移动
        }else if(prev.getNum() == this.num && !prev.merge){ //合并
            if(b){
                prev.merge = true;
                prev.setNum(2 * this.num);
                this.num = 0;
            }
            return true;
        }else{
            return false;
        }
    }
}
