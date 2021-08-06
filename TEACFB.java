import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.xml.transform.Templates;

public class TEACFB {
    private final static int [] key = {0xab1a16be, 0xc4163a89, 0x87e5b018, 0x65ed8705};
    private final static int kBit = 4;
    private final static long studNum = (0x68BA9C);
    private final static long iv = 1;
    private final static int kBit2 = 3;

    public static void main(String [] args)
    {
       String cipher = String.valueOf(Integer.parseInt(kbitcfbEncrypt(studNum,kBit,key, iv),2));
       System.out.println("Encrypted student number: "+cipher);
       System.out.println("===================================");
       String cipher2 = String.valueOf(Integer.parseInt(kbitofbEncrypt(studNum, kBit2, key, iv),2));
       System.out.println("Encrypted student number: "+cipher2);
       System.out.println("===================================");
      




       
      
    }

    public static String kbitofbEncrypt(long studNum,int kBit,int []key,long iv)
    {
        System.out.println("==Encryption==");
        System.out.println("kBitSize: "+kBit);
        System.out.println("IV: "+iv);
      System.out.println("PlainText: "+studNum);
      System.out.println("PlainText(Binary): "+Long.toBinaryString(studNum));

      //calculate number of loops to be performed
      int loops =(int) Math.ceil((double)Long.toBinaryString(studNum).length()/kBit2);
      System.out.println("Number of loops: "+loops);

        long iv1 = encrypt(iv, key);
        System.out.println("\n----------------------------");
        System.out.println("Round: 0 ");
        System.out.println("iv: "+Long.toBinaryString(iv));
        System.out.println("iv(encrypted): "+Long.toBinaryString(iv1));
       
       //first 3 bits of encrypted iv
       long first3iv = iv1>>>64-kBit2;
       System.out.println("First 3bits IV: "+Long.toBinaryString(first3iv));

       //first 3 bits of plaintext
       long first3plain = studNum>>>Long.toBinaryString(studNum).length()-kBit;
       System.out.println("First 3bits plain: "+Long.toBinaryString(first3plain));

       //shift first 3 bits out of plaintext
        //pad it with 0 first to make it 64bits
        studNum = studNum <<40;
        studNum = studNum <<kBit2;

        //cipher text (first4iv xor first4plain)
        long tempcipher = first3iv^first3plain;
        System.out.println("output: "+Long.toBinaryString(tempcipher));

        // create cipher stream
        long ciphertext = tempcipher;
        System.out.println("Ciphertext: "+Long.toBinaryString(ciphertext));


        iv1 = iv1<<kBit2;
        iv1 = iv1^first3iv;
        System.out.println("nextiv: "+Long.toBinaryString(iv1));
        System.out.println("next plain: "+Long.toBinaryString(studNum));

        for(int i = 1; i<loops;i++)
        {
            System.out.println("----------------------------");
            System.out.println("Round: "+i);
            System.out.println("iv: "+Long.toBinaryString(iv1));
            iv1 = encrypt(iv1, key);
            System.out.println("iv(encrypted): "+Long.toBinaryString(iv1));

            //first 3 bits of encrypted iv
            first3iv = iv1>>>64-kBit2;
            System.out.println("First 3bits IV: "+Long.toBinaryString(first3iv));
            //next 3 plaintext
            first3plain = studNum>>>Long.toBinaryString(studNum).length()-kBit2;
            System.out.println("First 3bits plain: "+Long.toBinaryString(first3plain));

            tempcipher = first3iv^first3plain;
            System.out.println("output: "+Long.toBinaryString(tempcipher));
            ciphertext = ciphertext<<kBit2;
            ciphertext = ciphertext ^tempcipher;
            System.out.println("Ciphertext: " +Long.toBinaryString(ciphertext));

            //create next iv
            iv1 = iv1<<kBit2;
            iv1 = iv1 ^ first3iv;
            System.out.println("nextiv: "+Long.toBinaryString(iv1));

            //shift plaintext
            studNum = studNum<<kBit;
            System.out.println("next plain: "+Long.toBinaryString(studNum));


        }
        System.out.println("===================================");


        return Long.toBinaryString(ciphertext);

    }

    public static String kbitcfbDecrypt(long cipher, int kBit, int[] key,long iv)
    {
        System.out.println("==Decryption==");
        System.out.println("kBitSize: "+kBit);
        System.out.println("IV: "+iv);
        System.out.println("Ciphertext: "+cipher);
        System.out.println("Ciphertext(binary): "+Long.toBinaryString(cipher));

        //calculate number of loops to be performed
        int loops = (int)Math.ceil((double)Long.toBinaryString(cipher).length()/kBit);
        System.out.println("Number of loops: "+loops);

        //encrypt iv
        long iv1 = decrypt(iv,key);
        System.out.println("\n----------------------------");
        System.out.println("Round: 0 ");
        System.out.println("iv: "+Long.toBinaryString(iv));
        System.out.println("iv(encrypted): "+Long.toBinaryString(iv1));

        //first 4 bits of encrypted iv
        long first4iv = iv1>>>64-kBit;
        System.out.println("First 4bits IV: "+Long.toBinaryString(first4iv));

        //first 4 bits of cipher
        long first4cipher = cipher>>>Long.toBinaryString(cipher).length()-kBit;
        System.out.println("First 4bits cipher: "+Long.toBinaryString(first4cipher));

        //shift first 4 bits out of ciphertext
        //pad it with 0 first to make 64 bits
        cipher = cipher<<40;
        cipher = cipher<<kBit;

        long tempplain = first4iv^first4cipher;
        System.out.println("output: "+Long.toBinaryString(tempplain));

        //create cipher stream
        long plaintext = tempplain;
        System.out.println("Plaintext: "+Long.toBinaryString(plaintext));

        //create iv for next step
        iv1 = iv1<<kBit;
        iv1 = iv1^cipher;
        System.out.println("nextiv: "+Long.toBinaryString(iv1));
        System.out.println("next cipher: "+Long.toBinaryString(cipher));

        for(int i =1;i<loops;i++)
        {
            System.out.println("----------------------------");
            System.out.println("Round: "+i);
            System.out.println("iv: "+Long.toBinaryString(iv1));

            iv1 = decrypt(iv1,key);
            System.out.println("iv(encrypted): "+Long.toBinaryString(iv1));

            //first 4 bits of encrypted iv
            first4iv = iv1 >>> 64-kBit;
            System.out.println("First 4bits IV: "+Long.toBinaryString(first4iv));

            //next 4 ciphertext
            first4cipher = cipher>>>Long.toBinaryString(cipher).length()-kBit;
            System.out.println("First 4bits cipher: "+Long.toBinaryString(first4cipher));

            //plaintext output
            tempplain = first4cipher^first4iv;
            System.out.println("output: "+Long.toBinaryString(tempplain));
            plaintext = plaintext<<kBit;
            plaintext = tempplain^plaintext;
            System.out.println("Plaintext: "+Long.toBinaryString(plaintext));

            //create next iv
            iv1 = iv1<<kBit;
            iv1 = iv1^cipher;
            System.out.println("nextiv: "+Long.toBinaryString(iv1));

            //shift ciphertext
            cipher = cipher<<kBit;
            System.out.println("next cipher: "+Long.toBinaryString(cipher));

        }



        return"";
    }

    public static String kbitcfbEncrypt(long studNum,int kBit, int []key,long iv)
    {
        System.out.println("==Encryption==");
        System.out.println("kBitSize: "+kBit);
        System.out.println("IV: "+iv);
        System.out.println("PlainText: "+studNum);
        System.out.println("PlainText(Binary): "+Long.toBinaryString(studNum));
        
        //calculate number of loops to be performed
        int loops =(int) Math.ceil((double)Long.toBinaryString(studNum).length()/kBit);
        System.out.println("Number of loops: "+loops);
        
        //encrypt iv
        long iv1 = encrypt(iv, key);
        System.out.println("\n----------------------------");
        System.out.println("Round: 0 ");
        System.out.println("iv: "+Long.toBinaryString(iv));
        System.out.println("iv(encrypted): "+Long.toBinaryString(iv1));

        //first 4 bits of encrypted iv
        long first4iv = iv1>>>64-kBit;
        System.out.println("First 4bits IV: "+Long.toBinaryString(first4iv));

        //first 4 bits of plaintext
        long first4plain = studNum>>>Long.toBinaryString(studNum).length()-kBit;
        System.out.println("First 4bits plain: "+Long.toBinaryString(first4plain));
        
        //shift first 4 bits out of plaintext
        //pad it with 0 first to make it 64bits
        studNum = studNum <<40;
        studNum = studNum <<kBit;

        //cipher text (first4iv xor first4plain)
        long tempcipher = first4iv^first4plain;
        System.out.println("output: "+Long.toBinaryString(tempcipher));


        //create cipher stream
        long ciphertext = tempcipher;
        System.out.println("Ciphertext: "+Long.toBinaryString(ciphertext));

        //create iv for next step
        iv1 = iv1<<kBit;
        iv1 = iv1^tempcipher;
        System.out.println("nextiv: "+Long.toBinaryString(iv1));
        System.out.println("next plain: "+Long.toBinaryString(studNum));

        for (int i =1; i<loops;i++)
        {
            System.out.println("----------------------------");
            System.out.println("Round: "+i);
            System.out.println("iv: "+Long.toBinaryString(iv1));
            iv1 = encrypt(iv1, key);

            

            System.out.println("iv(encrypted): "+Long.toBinaryString(iv1));

            //first 4 bits of encrypted iv
            first4iv = iv1>>>64-kBit;
            System.out.println("First 4bits IV: "+Long.toBinaryString(first4iv));

            //next 4 plaintext
            first4plain = studNum>>>Long.toBinaryString(studNum).length()-kBit;
            System.out.println("First 4bits plain: "+Long.toBinaryString(first4plain));

            //cipher text output
            tempcipher = first4iv^first4plain;
            System.out.println("output: "+Long.toBinaryString(tempcipher));
            ciphertext = ciphertext<<kBit;
            ciphertext = ciphertext ^tempcipher;
            System.out.println("Ciphertext: " +Long.toBinaryString(ciphertext));

            //create next iv
            iv1 = iv1 <<kBit;
            iv1 = iv1^tempcipher;
            System.out.println("nextiv: "+Long.toBinaryString(iv1));

            //shift plaintext
            studNum = studNum<<kBit;
            System.out.println("next plain: "+Long.toBinaryString(studNum));

        }
        System.out.println("===================================");
        
        


        return Long.toBinaryString(ciphertext);
    }

    private final static int DELTA = 0x9e3779b9;
    private final static int DECRYPT_SUM_INIT = 0xC6EF3720;
    private final static long MASK32 = (1L << 32) - 1;

    public static long encrypt(long in, int[] k) 
    {
        int v1 = (int) in;
        int v0 = (int) (in >>> 32);
        int sum = 0;
        for (int i = 0; i < 32; i++) 
        {
            sum += DELTA;
            v0 += ((v1 << 4) + k[0]) ^ (v1 + sum) ^ ((v1 >>> 5) + k[1]);
            v1 += ((v0 << 4) + k[2]) ^ (v0 + sum) ^ ((v0 >>> 5) + k[3]);
        }
        return (v0 & MASK32) << 32 | (v1 & MASK32);
        }

    
    public static long decrypt(long in, int [] k) 
    {
        int v1 = (int) in;
        int v0 = (int) (in >>> 32);
        int sum = DECRYPT_SUM_INIT;
        for (int i=0; i<32; i++) 
        {
            v1 -= ((v0<<4) + k[2]) ^ (v0 + sum) ^ ((v0>>>5) + k[3]);
            v0 -= ((v1<<4) + k[0]) ^ (v1 + sum) ^ ((v1>>>5) + k[1]);
            sum -= DELTA;
        }
        return (v0 & MASK32) << 32 | (v1 & MASK32);
    }

    
}
