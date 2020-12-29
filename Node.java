import javafx.util.Pair;

import java.util.LinkedList;

public class Node {
    private int data; //leaf- the number its represent. notLeaf- the pixel condition.
    private Node left;
    private Node right;
    private LinkedList<Image> images;
    private LinkedList<Integer> c;
    private double ig;
    private int best_condition;
    private LinkedList<Image> la;
    private LinkedList<Image> lb;


    public Node(int data,LinkedList<Image> images,LinkedList<Integer> c) {
        this.data=data;
        this.left=null;
        this.right=null;
        this.images=images;
        this.c=c;
        this.calculateIG(images,c);
    }

    private void calculateIG(LinkedList<Image> images, LinkedList<Integer> c) {
        //int[] allNumbers = new int[10];
        int NL=images.size();
        double HL=calculateHL(images,NL);
        //HERE WE HAVE THE HL !!!
        LinkedList<Image> La=new LinkedList<>();//who dont stands in the condition
        LinkedList<Image> Lb=new LinkedList<>();//who stands in the condition
        LinkedList<Image> Best_La=new LinkedList<>();
        LinkedList<Image> Best_Lb=new LinkedList<>();

        int Nla=0;
        int Nlb=0;
        double Hla=0;
        double Hlb=0;
        double minHX=-1;
        double curHX=0;
        int minX=0;
        Image curImage;
        int curPixel=0;
        int curPixelImage=0;
        for (int i=0;i<c.size();i++){
            curPixel=c.get(i);
            La.clear();
            Lb.clear();
            for(int j=0;j<NL;j++){
                curImage=images.get(j);
                curPixelImage=curImage.getPixel(curPixel);
                if(curPixelImage>128)
                    Lb.add(curImage);
                else
                    La.add(curImage);
            }
            Nla=La.size();
            Nlb=Lb.size();
            Hla=calculateHL(La,Nla);
            Hlb=calculateHL(Lb,Nlb);
            curHX=(((Nla*Hla)/NL)+((Nlb*Hlb)/NL));
            if((curHX < minHX)||(minHX==-1)) {
                minHX = curHX;
                minX=i;
                Best_La= (LinkedList<Image>) La.clone();
                Best_Lb=(LinkedList<Image>) Lb.clone();
            }
        }
        this.setIG((HL-minHX)*NL);
        this.setBest_condition(minX);
        this.setLa(Best_La);
        this.setLb(Best_Lb);
    }
    private double calculateHL(LinkedList<Image> images,int NL){
        int[] allNumbers = new int[10];
        for (int i = 0; i < 10; i++)//init the array
            allNumbers[i] = 0;
        for (int i = 0; i < NL; i++)//sum the mofaim of i
            allNumbers[images.get(i).getLabel()]++;
        double HL = 0;
        double log=0;
        double curAdd=0;
        for (int i = 0; i < 10; i++) {//calc the HL
            if(allNumbers[i] !=0){
                log=Math.log10(NL/allNumbers[i]);
                curAdd=(log*(allNumbers[i])/NL);
                HL=HL+curAdd;
            }
        }
        return HL;
    }
    //GETTERS
    public int getData(){
        return data;
    }
    public int getBest_condition(){
        return best_condition;
    }
    public Node getLeft(){
        return left;
    }
    public Node getRight(){
        return right;
    }
    public LinkedList<Image> getImages(){
        return images;
    }
    public LinkedList<Integer> getC(){
        return c;
    }
    public double getIG(){
        return ig;
    }
    public LinkedList<Image> getLa(){
        return la;
    }
    public LinkedList<Image> getLb(){
        return lb;
    }
    public int sizeImages(){
        return images.size();
    }

    //SETTERS
    public void setData(int new_data){
        this.data=new_data;
    }
    public void setLeft(Node left){
        this.left=left;
    }
    public void setRight(Node right){
        this.right=right;
    }
    public void setImages(LinkedList images){
        this.images=images;
    }
    public void setIG(double ig){
        this.ig=ig;
    }
    public void setBest_condition(int cond){
        this.best_condition=cond;
    }
    public void setLa(LinkedList<Image> la){
        this.la=la;
    }
    public void setLb(LinkedList<Image> lb){
        this.lb=lb;
    }
    public boolean isLeaf(){
        return ((left==null)&&(right==null));
    }
    public void printNode(){
        if(this.isLeaf())
            System.out.print("LEAF: "+data+" ");
        else
            System.out.print("COND: " +data+" ");
        if(left!=null)
            left.printNode();
        if(right!=null)
            right.printNode();
    }


}
