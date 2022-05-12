import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class FinalReceiver_20181658이성진 {
    public static void main(String[] args) {
    	String ok = ""; 
    	RecPhysicalLayer t6 = new RecPhysicalLayer(ok);
    	t6.start();   
    }
}

class CmnVarRec {
	public static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1); 
}

class RecPhysicalLayer extends Thread {
	private String t;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private OutputStream outputStream = null;
	private DataOutputStream dataOutputStream = null;
	private InputStream inputStream = null;
	private DataInputStream dataInputStream = null;
	
	public RecPhysicalLayer(String t) {
		this.t = t;
	}
	public void run() {
		try {
	        serverSocket = new ServerSocket(8000);
	        System.out.println("Sender로부터 데이터 전송받을 준비 완료");
			
	        socket = serverSocket.accept();
	        System.out.println("Sender 연결 완료");
	        System.out.println("socket : " + socket);
			
	        inputStream = socket.getInputStream();
	        dataInputStream = new DataInputStream(inputStream);
			
	        outputStream = socket.getOutputStream();
	        dataOutputStream = new DataOutputStream(outputStream);
			
	        while (true) {
	        		String clientMessage = dataInputStream.readUTF();
		            t = clientMessage;
		            break;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		System.out.println("---------------------------------------------------");
		System.out.println("Receiver의 PhysicalLayer");
		System.out.println("sender의 PhysicalLayer로부터 " + t + " 도착");
		String mlt[] = t.split("");
	    String after[] = new String[t.length()];
	    String eql = " ";
	        
	    for(int i =0; i<mlt.length; i++){
	        if(i == 0) {
	            if(mlt[i].equals("0")) {
	                after[i] = "0";
	                eql = mlt[i];
	            }
	            else if(mlt[i].equals("+")){
	                after[i] = "1";
	                eql = mlt[i];
	            }
	        }
	        else {
	            if(mlt[i].equals(eql)) {
	                after[i] = "0";
	                eql = mlt[i];
	            }
	            else {
	                after[i] = "1";
	                eql = mlt[i];
	            }
	        }
	    }   
	    System.out.print("MLT -> bit stream: ");
	    for(int i = 0; i<after.length; i++) {
	        System.out.print(after[i]);
	    }
	       
	    System.out.println();
	    String unstuff = String.join("", after);
	    System.out.println("Data Link로 " + unstuff + "전송");
	    try {
			CmnVarRec.queue.put(unstuff);   // queue에 넣어서 datalink로 전송 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    System.out.println("---------------------------------------------------");
	    RecDataLinkLayer t7 = new RecDataLinkLayer("");
	    t7.start(); 
	    try {
			t7.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			t = CmnVarRec.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("PhysicalLayer -> " + t + "전송 받음");
	    String mlt2[] = t.split("");
        String after2[] = new String[t.length()];
        String h = " ";
        String nonzero = " ";
        
        for(int j =0; j<mlt2.length; j++){
            if(j == 0) {
                if(mlt[j].equals("0")) {
                    after2[j] = mlt2[j];
                    h = mlt2[j];
                    nonzero = "-";
                }
                else {
                    after2[j] = "+";
                    h = "+";
                    nonzero = "-";
                }
            }
            else if(j > 0) {
                if((h.equals("0")) && (mlt2[j].equals("1")))  {
                    if(nonzero.equals("-")){
                        after2[j] = "+";
                        nonzero = "+";
                        h = "+";
                    }
                    else {
                        after2[j] = "-";
                        nonzero = "-";
                        h = "-";
                    }
                }
                else if((h.equals("0")) && (mlt2[j].equals("0"))) {
                    after2[j] = "0";
                    h = "0";
                }
                else if( (h.equals("+")) ) {
                    if(mlt2[j].equals("1")) {
                        after2[j] = "0";
                        nonzero = "+";
                        h = "0";
                    }
                    else {
                        after2[j] = "+";
                        nonzero = "+";
                        h = "+";
                    }
                }
                else if( (h.equals("-"))) {
                    if(mlt2[j].equals("1")) {
                        after2[j] = "0";
                        nonzero = "-";
                        h = "0";
                    }
                    else {
                        after2[j] = "-";
                        nonzero = "-";
                        h = "-";
                    }
                }
            }
        }
        System.out.print("MLT 처리 결과: ");
        for(int j = 0; j<after2.length; j++) {
            System.out.print(after2[j]);
        }
        System.out.println();
        StringBuffer result = new StringBuffer();
        for (int j = 0; j < after2.length; j++) {
           result.append( after2[j] );
        }
        String sendmessage2 = result.toString();
	    try {
			
	        inputStream = socket.getInputStream();
	        dataInputStream = new DataInputStream(inputStream);
			
	        outputStream = socket.getOutputStream();
	        dataOutputStream = new DataOutputStream(outputStream);
			
	        while (true) {
	        		dataOutputStream.writeUTF(sendmessage2);
	                dataOutputStream.flush();
	                System.out.println(sendmessage2 + "Sender PhysicalLayer에게 전송");
	                System.out.println("---------------------------------------------------");
	                break;
	        }
		
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (dataOutputStream != null) dataOutputStream.close();
	            if (outputStream != null) outputStream.close();
	            if (dataInputStream != null) dataInputStream.close();
	            if (inputStream != null) inputStream.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
}

class RecDataLinkLayer extends Thread {
	private String p;
	
	public RecDataLinkLayer(String p) {
		this.p = p;
	}
	
	public void run() {
		try {
			p = CmnVarRec.queue.take();
			System.out.println("DataLinkLayer -> " + p + " 전송 받음 "); 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String bitw = p;
        String bitunsuffer = bitw.replace("111110", "11111");
        System.out.println("unstuffing: " + bitunsuffer);
        System.out.println("NetworkLayer로 " + bitunsuffer + "전송"); 
        try {
			CmnVarRec.queue.put(bitunsuffer);   // queue에 넣어서 NetworkLayer 로 전송 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("---------------------------------------------------");
        RecNetworkLayer t8 = new RecNetworkLayer("");
        t8.start();
        try {
			t8.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			p = CmnVarRec.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("DataLinkLayer -> " + p + "전송 받음");
        String bitsuffer = p.replace("11111", "111110");
        System.out.println("DataLinkLayer -> bitstuffing: " + bitsuffer);
        try {
			CmnVarRec.queue.put(bitunsuffer);
			System.out.println("Simple Protocol로 MAC에게 전송");// queue에 넣어서 MacSublayer 로 전송 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			CmnVarRec.queue.take(); // Mac sublayer에서 꺼냄  
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        System.out.println("MAC Layer -> CSMA/CD Start");
        
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
                try {
        			CmnVarRec.queue.put(bitsuffer);
        			System.out.println("PhysicalLayer로" + bitsuffer + "전송");
        			System.out.println("---------------------------------------------------");
        		} catch (InterruptedException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
            }
            else {
                R = (int)(Math.random() * (2 * k-1)) +1;
                a = true;
                b = true;
            }
        }
        
    }
}

class RecNetworkLayer extends Thread {
	private String p;
	
	public RecNetworkLayer(String p) {
		this.p = p;
	}
	
	public void run() {
		try {
			p = CmnVarRec.queue.take();
			System.out.println("NetworkLayer -> " + p + " 전송 받음 "); 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("NetworkLayer -> " + p + "전송중");
		try {
			CmnVarRec.queue.put(p);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("---------------------------------------------------");
        RecTransportLayer t9 = new RecTransportLayer("");
        t9.start();
        try {
			t9.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			p = CmnVarRec.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("NetworkLayer -> " + p + " 지나가는중");
        System.out.println("---------------------------------------------------");
        try {
        	CmnVarRec.queue.put(p);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        		
    }
}

class RecTransportLayer extends Thread {
	private String p;
	static String ackmessage = "";
	String str;
	
	public RecTransportLayer(String p) {
		this.p = p;
	}
	
	public void run() {
		try {
			p = CmnVarRec.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Transport -> " + p + "도착");
		System.out.println("ACK 생성");
		System.out.println("Application으로 " + p + "전송");
        System.out.println("---------------------------------------------------");
        RecApplicaionLayer t10 = new RecApplicaionLayer("");
        try {
			CmnVarRec.queue.put(p);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        t10.start();
        try {
			t10.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("TransportLayer -> ACK to ASCII"); 
        String text = "ACK";
        for(int i=0; i< text.length(); i++) {
        	System.out.print(text.charAt(i) + "-> ");
        	int tmp = ((int)text.charAt(i));
        	System.out.print(tmp + "-> ");
        	str = Integer.toBinaryString(tmp);
        	System.out.println(str);
        	ackmessage += str;
        }
        System.out.println("Ack to ASCII-> " + ackmessage);
        System.out.println("NetworkLayer로 " + ackmessage + "전송");
        try {
			CmnVarRec.queue.put(ackmessage);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("---------------------------------------------------");
        
    }
}

class RecApplicaionLayer extends Thread {
	private String p;
	
	public RecApplicaionLayer(String p) {
		this.p = p;
	}
	
	public void run() {
		try {
			p = CmnVarRec.queue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Application -> " + p + "도착");
		System.out.println(p + "수신 확인");
        System.out.println("---------------------------------------------------");

        
    }
}