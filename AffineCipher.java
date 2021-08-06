import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

import java.io.FileWriter;
import java.io.IOException;

class Affine
{
    public static String abc ="abcdefghijklmnopqrstuvwxyz";
    public static String ABC = abc.toUpperCase();
    public static void main(String args[]) throws FileNotFoundException,IOException
    {
        int a=0;
        int b=0;
        final int[] aValues = new int[] {1,3,5,7,9,11,15,17,19,21,23,25};
        final int[] bValues = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
        
        boolean toggleEncDec = true;
        String text ="";
        for(int i=0;i<args.length;i++)
        {
            if(args[i].equals("-key"))
            {
                a = Integer.parseInt(args[i+1]);
                b =  Integer.parseInt(args[i+2]);
                if(a == 0 && b ==0)
                {
                    System.out.println("Brute forcing AFFINE CIPHER");
                }
                else if(checkKey(aValues,a)==false||checkKey(bValues, b)==false)
                {
                    System.out.println("Error! Key is invalid");
                    System.exit(0);
                }
            }

            if(args[i].equals("-encrypt"))
            {
                toggleEncDec = true;
            }
            if(args[i].equals("-decrypt"))
            {
                toggleEncDec = false;
            }


            if(args[i].equals("-in"))
            {
                
                String filename = args[i+1];
                File textFile = new File(filename);
                Scanner sc = new Scanner (textFile);

                text = sc.nextLine();
                while (sc.hasNextLine())
                {
                    text =text+"\n"+sc.nextLine();
                }
                sc.close();
            }

            if(args[i].equals("-out"))
            {
                String filename = args[i+1];
                FileWriter writer= new FileWriter(filename);
                if(a ==0 && b ==0)
                {
                    for(int x =0;x<aValues.length;x++)
                    {
                        for(int y = 0;y<bValues.length;y++ )
                        {
                                writer.write("\n\n------------("+aValues[x]+","+bValues[y]+")------------\n");
                                writer.write(decryptCipher(text, aValues[x], bValues[y]));
                        }
                    }
                }
                if(toggleEncDec)
                {
                   writer.write(encryptMessage(text, a, b)); 
                }else{
                    writer.write(decryptCipher(text, a, b));
                }
                
                writer.close();
            }

            
        }



        

    }

    public static String encryptMessage(String msg,int a,int b)
    {
        String C = "";
        char [] msgArr = msg.toCharArray();
        for (int i = 0;i<msgArr.length;i++)
        {
            if (ABC.indexOf(msgArr[i])!=-1 || abc.indexOf(msgArr[i])!=-1)
            {
                if(Character.isUpperCase(msgArr[i])){
                    // System.out.print(ABC.indexOf(msgArr[i]));
                    int index = ABC.indexOf(msgArr[i]);
                    int encryptIndex = ((index*a)+b)%26;
                    C = C+ABC.charAt(encryptIndex); 

                }else
                {
                    // System.out.print(abc.indexOf(msgArr[i]));
                    int index = abc.indexOf(msgArr[i]);
                    int encryptIndex = ((index*a)+b)%26;
                    C = C+abc.charAt(encryptIndex); 
                }
                
            }else
            {
                C+= msgArr[i];
            }
        }
        return C;
    }

    public static String decryptCipher(String C, int a, int b)
    {
        String msg = "";
        char[] cipherArr = C.toCharArray();
        int flag = 0;
        for (int i = 1; i <26;i++)
        {
            if(((a%26)*(i%26))%26==1)
            {
                flag = i;
            }
        }   

        for (int i =0; i<cipherArr.length;i++)
        {
            if(ABC.indexOf(cipherArr[i])!=-1 || abc.indexOf(cipherArr[i])!=-1)
            {
                if(Character.isUpperCase(cipherArr[i]))
                {
                    int index = ABC.indexOf(cipherArr[i]);
                    int decryptIndex = ((index - b)*flag);
                    int decryptFinal = Math.floorMod(decryptIndex, 26);
                    msg = msg+ABC.charAt(decryptFinal);
                }else
                {
                    int index = abc.indexOf(cipherArr[i]);
                    int decryptIndex = ((index - b)*flag);
                    int decryptFinal = Math.floorMod(decryptIndex, 26);
                    msg = msg+abc.charAt(decryptFinal);
                }
            }else
            {
                msg +=cipherArr[i];
            }
        }

        return msg;

    }
    public static boolean checkKey(int[]arr,int x)
    {
        
        boolean test =false;
        for (int element:arr){
            if(element == x)
            {
                test = true;
                break;
            }
        }
        return test;
    }
}