import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


class CmnVar {
	public static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1); 
}

class FinalSender_20181658이성진 {
	
	public static void main(String args[]) {
        
		Scanner s = new Scanner(System.in);
		String to = "";
		ApplicationLyaer t1 = new ApplicationLyaer(to);
		t1.start();
	}

}

class ApplicationLyaer extends Thread {
	private String t;
	
	public ApplicationLyaer(String t) {
		this.t = t;
	}
	
	public void run() {
		/*
		Scanner s = new Scanner(System.in);
		System.out.println("전송하고 싶은 8자리 비트 입력: ");
		String message = s.nextLine();
		*/
		String message = "11111111";
		System.out.println("ApplicationLayer -> " + message + " 전송");
		System.out.println("---------------------------------------------------");
		try {
			CmnVar.queue.put(message);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		TransportLayer t2 = new TransportLayer("");
		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t  = CmnVar.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("ApplicaionLayer ->" + t + " 메시지를 받았다. 잘 도착했다고 한다 ! ! !  끗 ");
		
	}
}

class TransportLayer extends Thread {
	private String t;
	String message;
	
	public TransportLayer(String t) {
		this.t = t;
	}
	
	public void run() {
		try {
			message = CmnVar.queue.take();
			System.out.println("TransportLayer -> " + message + " 전송 받음 "); 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("TransportLayer -> " + message + " 전송");
		try {
			CmnVar.queue.put(message);
			System.out.println("stop.. wait! ");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("---------------------------------------------------");
		NetworkLayer t33 = new NetworkLayer("");
		t33.start();
		try {
			t33.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
        	t = CmnVar.queue.take();
        	System.out.println("TransportLayer -> " + t + "전송 받음");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String check = t;
		String a = check.substring(0,7);
		String b = check.substring(7,14);
		String c = check.substring(14,21);
		int binaryToDecimala = Integer.parseInt(a, 2);
		int binaryToDecimalb = Integer.parseInt(b, 2);
		int binaryToDecimalc = Integer.parseInt(c, 2);
		char g = ((char)binaryToDecimala);
		char f = ((char)binaryToDecimalb);
		char t = ((char)binaryToDecimalc);
		String gg = g + "";
		String ff = f + "";
		String tt = t + "";
		String R = gg + ff + tt;
		System.out.println(a + "->" + binaryToDecimala + "-> " + gg );
		System.out.println(b + "->" + binaryToDecimalb + "-> " + ff );
		System.out.println(c + "->" + binaryToDecimalc + "-> " + tt );
		System.out.println(R);
		if ( R.equals("ACK")) {
			System.out.println("수신 확인");
		}
		try {
			CmnVar.queue.put("수신확인");
			System.out.println("ApplicationLayer에게 수신확인 보냄");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("---------------------------------------------------");
		
	}
}

class NetworkLayer extends Thread {
	private String t;
	String message;
	
	public NetworkLayer(String t) {
		this.t = t;
	}
	
	public void run() {
		try {
			message = CmnVar.queue.take();
			System.out.println("NetworkLayer -> " + message + " 전송 받음 "); 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("NetworkLayer -> " + message + " 전송");
		try {
			CmnVar.queue.put(message);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("---------------------------------------------------");
		DataLinkLayer t3 = new DataLinkLayer("");
		t3.start();
		try {
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
        	t = CmnVar.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("NetworkLayer -> " + t + "전송");
		System.out.println("---------------------------------------------------");
		try {
        	CmnVar.queue.put(t);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}

class DataLinkLayer extends Thread {
	private String p;
	String message;
	
	public DataLinkLayer(String p) {
		this.p = p;
	}
	
	public void run() {
		try {
			message = CmnVar.queue.take();
			System.out.println("DataLinkLayer -> " + message + " 전송 받음 "); 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		p = message;
        String bitw = p;
        String bitsuffer = bitw.replace("11111", "111110");
        System.out.println("DataLinkLayer -> bitstuffing: " + bitsuffer);
        System.out.println("Simple protocol로 Mac Layer에게로 전송");
        System.out.println("MacLayer -> csma/cd start: ");
        
        Scanner s = new Scanner(System.in);
        Random rd = new Random();
        boolean a = true;
        boolean b = true;
        int rdnum;
        int perrdnum;
        int rdcollision;
        int R;
        int Success = 0;
        int Kmax = 1;
        int k = 0;
        while(k<=Kmax) {
            while(b) {
                perrdnum = rd.nextInt(10)+1;
                if(perrdnum % 2 == 1) {
                    System.out.println("persistence is idel");
                    b = false;
                }
                else {
                    System.out.println("persistence is busy, wait, Try again");
                    b = true;
                }
            }
            while(a) {
                System.out.println("Transmission done?"); 
                for(int i=0;i<1;i++) {
                    rdnum = rd.nextInt(10)+1;
                    rdcollision = rd.nextInt(10)+1;
                    if(rdnum % 2 == 1) { //홀수면 transmsiion done...
                        System.out.println("Yes !!! done !! ");
                        if(rdcollision % 2 == 1) { //홀수면 
                            System.out.println("Not Collision");
                            Success = 1;
                            k = Kmax + 1;
                        }
                        else {
                            System.out.println("Collision");
                            System.out.println("Send a jamming signal");
                            Success = 0;
                            k = k +1;
                        }
                        a = false;
                    }
                    else {
                        System.out.println("NO !!!! Transmit and receive");
                        i=0; 
                    }
                } 
            } 
            if(k>Kmax && Success == 0) {
                //System.out.println("Abort !");
            	k--;
            }
            else if(k>Kmax && Success == 1){
                System.out.println("Success!!");
                System.out.println("MacLayer -> csma/cd end: ");
            }
            else {
                R = (int)(Math.random() * (2 * k-1)) +1;
                a = true;
                b = true;
            }
        }
        try {
			CmnVar.queue.put(bitsuffer);
			System.out.println("PhysicalLayer로" + bitsuffer + "보냄");
			System.out.println("---------------------------------------------------");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        PhysicalLayer t4 = new PhysicalLayer("");
		t4.start();
		try {
			t4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			p = CmnVar.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//String bitw2 = PhysicalLayer.getackmessage();
		System.out.println("DataLinkLayer -> " + p + " Physical로 부터 전송받음" );
        String bitunsuffer = p.replace("111110", "11111");
        System.out.println("unstuffing: " + bitunsuffer);
        System.out.println("simple protocol로 " + bitunsuffer + "보냄");
        try {
        	CmnVar.queue.put(bitunsuffer);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("---------------------------------------------------");
        
    }
}

class PhysicalLayer extends Thread {
	private String t;
	private Socket socket = null;
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;
    private InputStream inputStream;
    private DataInputStream dataInputStream;
    static String ackmessage = ""; 
    String message;
	
	public PhysicalLayer(String t) {
		this.t = t;
	}	
	public void run() {
		try {
			message = CmnVar.queue.take();
			System.out.println("PhysicalLayer -> " + message + " 전송 받음 "); 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		t = message;
        String mlt[] = t.split("");
        String after[] = new String[t.length()];
        String h = " ";
        String nonzero = " ";
        
        for(int i =0; i<mlt.length; i++){
            if(i == 0) {
                if(mlt[i].equals("0")) {
                    after[i] = mlt[i];
                    h = mlt[i];
                    nonzero = "-";
                }
                else {
                    after[i] = "+";
                    h = "+";
                    nonzero = "-";
                }
            }
            else if(i > 0) {
                if((h.equals("0")) && (mlt[i].equals("1")))  {
                    if(nonzero.equals("-")){
                        after[i] = "+";
                        nonzero = "+";
                        h = "+";
                    }
                    else {
                        after[i] = "-";
                        nonzero = "-";
                        h = "-";
                    }
                }
                else if((h.equals("0")) && (mlt[i].equals("0"))) {
                    after[i] = "0";
                    h = "0";
                }
                else if( (h.equals("+")) ) {
                    if(mlt[i].equals("1")) {
                        after[i] = "0";
                        nonzero = "+";
                        h = "0";
                    }
                    else {
                        after[i] = "+";
                        nonzero = "+";
                        h = "+";
                    }
                }
                else if( (h.equals("-"))) {
                    if(mlt[i].equals("1")) {
                        after[i] = "0";
                        nonzero = "-";
                        h = "0";
                    }
                    else {
                        after[i] = "-";
                        nonzero = "-";
                        h = "-";
                    }
                }
            }
        }
        System.out.print("MLT 처리 결과: ");
        for(int i = 0; i<after.length; i++) {
            System.out.print(after[i]);
        }
        System.out.println();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < after.length; i++) {
           result.append( after[i] );
        }
        String sendmessage = result.toString();
      
        try {
        	socket = new Socket("localhost", 8000);
        	outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
			
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
        	while(true) {
        		dataOutputStream.writeUTF(sendmessage);
                dataOutputStream.flush();
                System.out.println(sendmessage + "receiver PhysicalLayer로 전송");
                break;
        	}
        } catch(IOException e) {
        	e.printStackTrace();
        }finally {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
        	while(true) {
        		String ServerMessage = dataInputStream.readUTF();
	            t = ServerMessage;
                break;
        	}
        } catch(IOException e) {
        	e.printStackTrace();
        }finally {
            try {
                if (dataOutputStream != null) dataOutputStream.close();
                if (outputStream != null) outputStream.close();
                if (dataInputStream != null) dataInputStream.close();
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ackmessage = t;
        System.out.println("Receiver Physical Layer로부터 " + ackmessage + "받음");
        String tt = ackmessage;
        String mlt2[] = tt.split("");
	    String after2[] = new String[tt.length()];
	    String eql = " ";
	        
	    for(int i =0; i<mlt2.length; i++){
	        if(i == 0) {
	            if(mlt2[i].equals("0")) {
	                after2[i] = "0";
	                eql = mlt2[i];
	            }
	            else if(mlt2[i].equals("+")){
	                after2[i] = "1";
	                eql = mlt2[i];
	            }
	        }
	        else {
	            if(mlt2[i].equals(eql)) {
	                after2[i] = "0";
	                eql = mlt2[i];
	            }
	            else {
	                after2[i] = "1";
	                eql = mlt2[i];
	            }
	        }
	    }
	    System.out.print("MLT -> bit stream: ");
	    for(int i = 0; i<after2.length; i++) {
	        System.out.print(after2[i]);
	    }
	       
	    System.out.println();
	    String unstuff = String.join("", after2);
	    System.out.println("Data Link로 " + unstuff + "전송");
	    try {
			CmnVar.queue.put(unstuff);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    System.out.println("---------------------------------------------------");
	}
}
