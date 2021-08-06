import javax.lang.model.util.ElementScanner14;



public class LinearDES
{
    
    
    public static void main (String[]args)
    {
        System.out.println("Encryption:");
        System.out.println("Key\\Message:   0000 0001 0010 0011 0100 0101 0110 0111 1000 1001 1010 1011 1100 1101 1110 1111");
        System.out.print(" 00            ");
       for (int i = 0;i<16;i++)
       {
        System.out.print(Encrypt(Integer.toBinaryString(i),"00")+ " ");
       }
       System.out.println();
       System.out.print(" 01            ");
       for (int i = 0;i<16;i++)
       {
        System.out.print(Encrypt(Integer.toBinaryString(i),"01")+ " ");
       }
       System.out.println();
       System.out.print(" 10            ");
       for (int i = 0;i<16;i++)
       {
        System.out.print(Encrypt(Integer.toBinaryString(i),"10")+ " ");
       }
       System.out.println();
       System.out.print(" 11            ");
       for (int i = 0;i<16;i++)
       {
        System.out.print(Encrypt(Integer.toBinaryString(i),"11")+ " ");
       }

       System.out.println("\n\nDecryption:");
       System.out.println("Key\\Message:   0000 0001 0010 0011 0100 0101 0110 0111 1000 1001 1010 1011 1100 1101 1110 1111");
       System.out.print(" 00            ");
      for (int i = 0;i<16;i++)
      {
       System.out.print(Decrypt(Integer.toBinaryString(i),"00")+ " ");
      }
      System.out.println();
      System.out.print(" 01            ");
      for (int i = 0;i<16;i++)
      {
       System.out.print(Decrypt(Integer.toBinaryString(i),"01")+ " ");
      }
      System.out.println();
      System.out.print(" 10            ");
      for (int i = 0;i<16;i++)
      {
       System.out.print(Decrypt(Integer.toBinaryString(i),"10")+ " ");
      }
      System.out.println();
      System.out.print(" 11            ");
      for (int i = 0;i<16;i++)
      {
       System.out.print(Decrypt(Integer.toBinaryString(i),"11")+ " ");
      }


      //verify
      System.out.print("\n\nVerification:");
      Verify("1100", "1000", "0100", "0000");
      Verify("1010","1000","0010","0000");
      Verify("1001","1000","0001","0000");
      Verify("0110","0100","0010","0000");
      Verify("0101","0100","0001","0000");
      Verify("0011","0010","0001","0000");
      Verify("0111","0100","0010","0001");
      Verify("1011","1000","0010","0001");
      Verify("1101","1000","0100","0001");
      Verify("1110","1000","0100","0010");
      Verify("1111","1000","0100","0011");
    

    }


    public static void Verify(String E, String x, String y, String z)
    {
        String [] Keys = {"00","01","10","11"};
        System.out.printf("\n\nExpression             E(%s) = E(%s) ^ E(%s) ^ E(%s)",E,x,y,z);
        for(String k:Keys)
        {
            String x1 = Encrypt(x, k);
            String y1 = Encrypt(y, k);
            String z1 = Encrypt(z, k);
            System.out.printf("\nVerified with Key: %s -> %s =    %s  ^   %s   ^  %s     Verified: %s",k,Encrypt(E, k),x1,y1,z1,XOR(x1,y1,z1));
        }
        
    }
   

    public static String XOR (String x,String y,String z)
    {  
        StringBuffer sb = new StringBuffer();
        for(int i =0;i<x.length();i++)
        {
            sb.append((x.charAt(i)^y.charAt(i)));
        }
        StringBuffer sb1 = new StringBuffer();
        for(int i =0;i<x.length();i++)
        {
            sb1.append((sb.charAt(i)^z.charAt(i)));
        }
        return sb1.toString();
    }

    
    public static String Encrypt(String x,String key)
    {

        x = fourbitrotateLeft(x);
        //pad leading 0 back to x;
       
        if (x.length()<4)
        {
            x = leftPadding(x, '0',4);
        }
        //System.out.println("After left rotate: "+x);
        String A0 = x.substring(0, 2);
        String B0 = x.substring(2,4);
        //System.out.println("A0:"+A0+" B0:"+B0);

        String k1 = key.substring(0,1)+key.substring(0,1)+key.substring(0,1);
        String z = functionZ(B0,k1);
        //System.out.println("z1: "+z);
        
        //z xor with A0 to get B1
       //System.out.println(z+" "+A0);
       String B1 = Integer.toString(Integer.parseInt(A0)^Integer.parseInt(z));
       if(B1.length()<2)
       {
           B1 = leftPadding(B1, '0', 2);
       }
      //System.out.println(B1);

       String k2 = key.substring(1,2)+key.substring(1, 2)+key.substring(1, 2);
       String A1 = B0;
       String z1 = functionZ(B1,k2);
       String B2 = Integer.toString(Integer.parseInt(z1)^Integer.parseInt(A1));
       if(B2.length()<2)
       {
           B2 = leftPadding(B2, '0', 2);
       }
      // System.out.print(B2);
      String A2 = B1;  
      String B2A2 = B2+A2;
     // System.out.print(B2A2);

     String cipher = fourbitrotateRight(B2A2);
     if (cipher.length()<4)
     {
         cipher = leftPadding(cipher, '0', 4);
     }
     //System.out.print(cipher);
     return cipher;
    }

    public static String Decrypt(String x, String key)
    {
        x = fourbitrotateLeft(x);
        //pad leading 0 back to x;
       
        if (x.length()<4)
        {
            x = leftPadding(x, '0',4);
        }
        //System.out.println("After left rotate: "+x);
        String A0 = x.substring(0, 2);
        String B0 = x.substring(2,4);
        //System.out.println("A0:"+A0+" B0:"+B0);
        String k2 = key.substring(1,2)+key.substring(1, 2)+key.substring(1, 2);
        String k1 = key.substring(0,1)+key.substring(0,1)+key.substring(0,1);
        String z = functionZ(B0,k2);
        //System.out.println("z1: "+z);
        
        //z xor with A0 to get B1
       //System.out.println(z+" "+A0);
       String B1 = Integer.toString(Integer.parseInt(A0)^Integer.parseInt(z));
       if(B1.length()<2)
       {
           B1 = leftPadding(B1, '0', 2);
       }
      //System.out.println(B1);

       
       String A1 = B0;
       String z1 = functionZ(B1,k1);
       String B2 = Integer.toString(Integer.parseInt(z1)^Integer.parseInt(A1));
       if(B2.length()<2)
       {
           B2 = leftPadding(B2, '0', 2);
       }
      // System.out.print(B2);
      String A2 = B1;  
      String B2A2 = B2+A2;
     // System.out.print(B2A2);

     String msg = fourbitrotateRight(B2A2);
     if (msg.length()<4)
     {
         msg = leftPadding(msg, '0', 4);
     }
     //System.out.print(cipher);
     return msg;
    }

    public static String fourbitrotateRight(String x)
    {
        int number = Integer.parseInt(x,2);
        switch (number){
            case 0:
                number = 0;
                break;
            case 1:
                number = 8;
                break;
            case 2:
                number = 1;
                break;
            case 3:
                number = 9;
                break;
            case 4:
                number = 2;
                break;
            case 5:
                number = 10;
                break;
            case 6:
                number = 3;
                break;
            case 7:
                number = 11;
                break;
            case 8:
                number = 4;
                break;
            case 9:
                number = 12;
                break;
            case 10:
                number = 5;
                break;
            case 11:
                number = 13;
                break;
            case 12:
                number = 6;
                break;
            case 13:
                number = 14;
                break;
            case 14:
                number = 7;
                break;
            case 15:
                number = 15;
                break; 
        }
        return Integer.toBinaryString(number);
    }

    public static String fourbitrotateLeft(String x)
    {
        int number = Integer.parseInt(x,2);
        switch(number){
            case 0:
                number = 0;
                break;
            case 1:
                number = 2;
                break;
            case 2:
                number = 4;
                break;
            case 3:
                number = 6;
                break;
            case 4:
                number = 8;
                break;
            case 5:
                number = 10;
                break;
            case 6:
                number = 12;
                break;
            case 7:
                number = 14;
                break;
            case 8:
                number = 1;
                break;
            case 9:
                number = 3;
                break;
            case 10:
                number = 5;
                break;
            case 11:
                number = 7;
                break;
            case 12:
                number = 9;
                break;
            case 13:
                number = 11;
                break;
            case 14:
                number = 13;
                break;
            case 15:
                number = 15;
                break;
        }
        return Integer.toBinaryString(number);
    }


    public static String functionZ(String x, String key)
    {
        String z = "";
        String expandedX = x.substring(0,1)+x.substring(1, 2)+x.substring(0, 1);
        //System.out.println("x1x2x1: "+expandedX);
        //System.out.println(expandedX+" "+key);
        //xor key(Y1Y2Y3) with expanded X(X1X2X1)
        int L = Integer.parseInt(expandedX)^Integer.parseInt(key);

        String L1 = Integer.toString(L);
        if(L1.length()<3)
        {
            L1=leftPadding(L1, '0', 3);
        }
        //System.out.println("L1L2L3: "+L1);
        String J1temp = L1.substring(1, 2);
        String J2temp = L1.substring(2,3);
       // System.out.println("J1J2Temp: "+J1temp+" "+J2temp);
        int J1 = Integer.parseInt(J1temp)^1;
        int J2 = Integer.parseInt(J2temp)^1;
       // System.out.println("J1J2: "+Integer.toString(J1)+Integer.toString(J2));
        //rotate left on J1 and J2 to get Z
        z=Integer.toString(J2)+Integer.toString(J1);
        return z;
    }

    public static String leftPadding(String input, char ch, int L)
    {
        String result = String.format("%"+L+"s", input).replace(' ',ch);
        return result;
    }


}

