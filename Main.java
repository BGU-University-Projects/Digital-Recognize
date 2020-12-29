import javafx.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Main {
    public static Image[] read_csv(String path, int n)  {

        Image[] data = new Image[n];
        String s;
        Scanner sc = null;
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sc.useDelimiter(",");
        int[] curr_data = new int[784];
        int curr_label=-1;
        for (int i = 0; i < n; i++) {
            s = sc.next();
            s = s.replaceAll("\\s+", "");
            curr_label = Integer.parseInt(s);
            for (int j = 1; j < 784; j++) {
                s = sc.next();
                curr_data[j-1] = Integer.parseInt(s);
            }
            sc.useDelimiter("\n");
            s = sc.next();
            sc.useDelimiter(",");
            curr_data[783] = Integer.parseInt(s.substring(1));
            data[i] = new Image(curr_data, curr_label);
        }
        sc.close();  //closes the scanner
        return data;
    }
    public static int mostCommon(LinkedList<Image> images){
        int maxNumber=0;
        if(images.size()==3)
            maxNumber=0;
        int[] allNumbers=new int[10];
        int curNumber;
        int totalSize=images.size();
        for(int j=0;j<10;j++)
            allNumbers[j]=0;
        for(int i=0;i<totalSize;i++) {
            curNumber = images.get(i).getLabel();
            allNumbers[curNumber] = (allNumbers[curNumber]) + 1;
        }
        for(int j2=0;j2<10;j2++)
            if(allNumbers[j2]>allNumbers[maxNumber])
                maxNumber=j2;
        return maxNumber;
    }
    public static LinkedList<Integer> random_P(int p,int allLength){
        int how_much=p*allLength/100;
        int randomNum;
        LinkedList<Integer> generatedNumbers = new LinkedList<Integer>();
        while (generatedNumbers.size() < how_much){
            randomNum=(int) (Math.random() * (allLength));
            if(! (generatedNumbers.contains(randomNum)))
                generatedNumbers.add(randomNum);
        }
//        while( ! generatedNumbers.isEmpty())
//            System.out.println(generatedNumbers.pop());
        return generatedNumbers;
    }
    public static LinkedList<LinkedList<Image>> makeArrays(LinkedList<Integer> numbers,Image[] allimages){
        LinkedList<Image> val_set = new LinkedList<Image>();
        LinkedList<Image> rest_set= new LinkedList<Image>();
        for(int i=0;i<allimages.length;i++){
            if (numbers.contains(i))
                val_set.add(allimages[i]);
            else
                rest_set.add(allimages[i]);
        }
        LinkedList<LinkedList<Image>> finish= new LinkedList<LinkedList<Image>>();
        finish.addFirst(val_set);
        finish.addFirst(rest_set);
        return finish;
    }
    public static LinkedList<Integer> make_conditions(int length){
        LinkedList<Integer> kk=new LinkedList<Integer>();
        for(int i=0;i<length;i++)
            kk.addLast(i);
        return kk;
    }
    public static void expandNode(Node cur){
        //System.out.println(cur.getC());
        //LinkedList<Integer> newConds;

        //newConds= (LinkedList<Integer>) cur.getC().clone();
        //newConds.remove(cur.getBest_condition());
        int mostCommon_A=mostCommon(cur.getLa());
        int mostCommon_B=mostCommon(cur.getLb());

        Node la=new Node(mostCommon_A,cur.getLa(), cur.getC());
        Node lb=new Node(mostCommon_B,cur.getLb(), cur.getC());
        cur.setLeft(la);
        cur.setRight(lb);
        cur.setData(cur.getBest_condition());
    }
    public static Node buildTree(Node root,int T,LinkedList<Image> images,LinkedList<Integer> C){
        LinkedList<Node> leafs=new LinkedList<>();//TODO can be better runtime with heap
        Node root_to_return=root;
        leafs.add(root_to_return);
        for(int i=0;i<T;i++){
            int indexMaxIG=0;
            double MaxIG=0;
            Node max_Node=null;
            Node cur_Node;
            for(int j=0;j<leafs.size();j++){
                cur_Node=leafs.get(j);
                if(cur_Node.getIG()>MaxIG){
                    MaxIG=cur_Node.getIG();
                    indexMaxIG=j;
                    max_Node=cur_Node;
                }
            }
            //we have the most IG leaf, now we need to replace it with the most good cond.
            // attach to him create 2 child Nodes, delete it from the leafs and add the childs
            //System.out.println(leafs.size());

            if(max_Node==null)
                i=T;
            else {
                expandNode(max_Node);
                leafs.remove(max_Node);
                leafs.add(max_Node.getLeft());
                leafs.add(max_Node.getRight());
            }
        }
    return root;
    }
    public static int validate(Node Tree, LinkedList<Image> images) {
        int output = 0;
        int total=images.size();
        Image cur_test=null;
        for (int i=0;  i< total ; i++) {
            cur_test = images.get(i);
            //if(cur_test.getPixel(150)==50)
               // System.out.println("w");
            //System.out.println("we entered: " + cur_test.getLabel());
            //System.out.println("returned from check: " + check(cur_test, Tree));
            if (check(cur_test, Tree) == cur_test.getLabel()){
                //System.out.println("we entered: " + cur_test.getLabel());
                //System.out.println("returned from check: " + check(cur_test, Tree));
                output = output + 1;
            }
//            else{
//                System.out.println("we entered: " + cur_test.getLabel());
//                System.out.println("returned from check: " + check(cur_test, Tree));
//            }
        }
        return (output*100/total);
    }
    public static int check(Image im, Node Tree){
        if (Tree.isLeaf()) {
            //if(Tree.getData()!=im.getLabel())
                //System.out.println("lll");
            return Tree.getData();
        }
        int curCond=Tree.getData();
        int curpixel=im.getPixel(curCond);
        if (curpixel > 128)
            return check (im, Tree.getRight());
        return check(im, Tree.getLeft());
    }

    public static void main(String[] args) {
        //System.out.println("START");
        int totalPixelsinImage=784;
        int sizeInputToRead=6000;
        int L=parseInt(args[0]);
        int p=10;//TODO to check from where
        String path="C:\\Users\\User\\IdeaProjects\\Digital_Recognition\\mnist_train.csv";//TODO where should be
        Image[] allImages=read_csv(path,sizeInputToRead);
//        for(int i=0;i<allImages.length;i++)
//            allImages[i].printImage();
        int inputLength=allImages.length;
       System.out.println(inputLength);
        LinkedList<LinkedList<Image>> both=makeArrays(random_P(p,inputLength),allImages);//TODO may be better on RUNTIME
        LinkedList<Image> validation_set=both.getLast();
        LinkedList<Image> training_Images=both.getFirst();

        LinkedList<Integer> C=make_conditions(totalPixelsinImage);
        int most_Common=mostCommon(training_Images);//TODO to put in the make arrays for better run time
        Node myTree= new Node(most_Common,training_Images,C);
        int T=0;
        int best_success=0;
        int best_T=0;
        int cur_success=0;
        Node cur_Tree;
        L=3;
        for(int i=0;i<=L;i++) {//TODO we can save the tree in run time and not make a new tree for each 2^i
            T = (int) Math.pow(2, L);
            cur_Tree = buildTree(myTree,T, training_Images, C);
            //cur_Tree.printNode();

            cur_success=validate(cur_Tree,validation_set);

            System.out.println("");
            System.out.println("------------------"+ cur_success +" % ------------------");
            if (best_success < cur_success){
                best_T=T;
                best_success=cur_success;
            }
        }
        System.out.println("best T is: "+best_T);
        LinkedList<Image> allImages2=new LinkedList<>();
        for(int i=0;i<allImages.length;i++)
            allImages2.add(allImages[i]);

        cur_Tree=buildTree(myTree,best_T,allImages2,C);//TODO update the root
        //return cur_Tree;
        }
}


