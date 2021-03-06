package person;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import bulletin.*;
import status.*;

public class Student extends Person {
	String score;
	int doc_count=0;
	static int count =0;
	
	public String getNumber() {
		return this.number;
	}
	public Student(String name, String number, String score) {
		super(name, number);
		this.score = score;
	}

	public void see_cur_status() { // search for student's current status
		
		if(Status.download() == false) {
			System.out.println("Status not found ");
			return;
		}
		
		
		
		for(Status b : Status.status)
			if(this.number.equals(b.getNumber()))
			{
				for(int i=0;i<5;i++)
					System.out.println();
				System.out.println("**********" + this.number + "'s status**********");
				b.show_info();
				System.out.println("**********Document List**********");
				for (Document d : b.getDocument()) {
					d.show_info();
				}
				return;
			}
		
		System.out.println("Student's Status has not found ");
		return;		
	}

	public void register_document() { // register document
		LinkedList<Document> document=new LinkedList<>();
		String col_name=null;
		String doc_type;
		int findFlag = 0;
		int listIndex = 0;
		
		if(Status.download() == false) {
			System.out.println("Status not found ");
			return;
		}
	
		
		if(Status.first_application_check()!=true) {
			System.out.println("It is not the period for first apply ");
			return;
		}
		
			
		
		
		for(Status s: Status.status)
		{
			if(number.equals(s.getNumber())) {
				col_name=s.getApplication().get_coll_name();
				findFlag =1;
				break;
			}
			listIndex++;
		}
		
		if(Status.status.get(listIndex).getStat1() != 1 )
		{
			System.out.println("Not appropriate step");
			return;
		}
		if(findFlag == 0)
		{
			System.out.println("Student's stat has not found");
			return;
		}
		
	
			
		// student status exist / step : 1 / student's stat1 : 1
		Scanner sc = new Scanner(System.in);
			System.out.println("Please input the type of document\n" + "otherwise input quit to Quit");
			doc_type=sc.next();
			
			if(doc_type.equals("quit")){
				Status.upload();
				return;
			}
			for(Status s: Status.status)
			{
				if(number.equals(s.getNumber())) {
					document=s.getDocument();
					Document temp=new Document(name,number,col_name,doc_type);
					if(s.doc_count==0) {
						s.doc_count++;
						document.add(temp);
						s.setDocument(document);
					}
					else
						s.addDocument(temp);
				}
			}
			
		Status.upload();
	}


	public void cancel_apply() { // canceling application
		String quitOption;
		if(Status.download() == false) {
			System.out.println("Status not found ");
			return;
		}
		
		if(!Status.first_application_check() && !Status.final_application_check()) {
			System.out.println("Not appropriate step");
			Status.upload();
			return;
		}
		
		
		Scanner sc = new Scanner(System.in);

			System.out.println("Input quit to quit cancel_apply(Otherwise press any key to continue)");
			quitOption = sc.next();
			if (quitOption.equals("quit")) {
				Status.upload();
				return;
			}
		

		
		ListIterator<Status> itr=Status.status.listIterator();
		while(itr.hasNext()) {
			if(this.getNumber().equals(itr.next().getNumber())) {
				itr.remove();
				Status.upload();
				System.out.println("You have the canceled the application");
				return;
			}		
		}

		Status.upload();
		System.out.println("You have not applied for exchange student");
		return;
	}

	
	public boolean firstapply() { // see applicable Bulletin and apply
		int length, select;
		String quitOption;

		if (Status.download() == false && Student.count != 0) {
			System.out.println("Status not found ");
			return false;
		}
		count++;

		for (Status s : Status.status) {
			if (number.equals(s.getNumber())) {
				if (s.getStat1() == 1) {
					System.out.println("Already Applied");
					return false;
				}
				continue;
			}
		}
		if (Status.first_application_check() == false) {
			System.out.println("It is not the period for first apply ");
			return false;
		}

		Scanner sc = new Scanner(System.in);

		Bulletin.download();
		length = see_Applicable_bull(); // from 'applicable bulletin : database'
		
		System.out.println("Input quit to Quit(Otherwise press any key to continue)");
		quitOption = sc.next();
		if (quitOption.equals("quit")) {
			Status.upload();
			return true;
		}

		System.out.print("Input Bull you apply : ");
		select = sc.nextInt();
		if (select < 0 || select > length) {
			return false; // Select val. error catch-> do - while until proper val.
		}

		Status.add_status_to_list(name, number, 1, 0, 0, Bulletin.bulletin.get(select));
		System.out.println(Bulletin.bulletin.get(select).get_bull_name());

		Status.upload();
		return true;
	}
	
	public int see_Applicable_bull() {
		int count = -1;
		Bulletin.download();
		for(Bulletin b : Bulletin.bulletin){
			count++; 
			if (score.compareTo(b.getRequiredScore()) <= 0) 
			{
				System.out.print(count+"\t");
				b.show_info();		
			}
		}
		return count;
	}

	
	public boolean finalapply() { // final application
		String quitOption;
		if(Status.download() == false) {
			System.out.println("Status not found ");
			return false;
		}
		for(Status s: Status.status) {
			if (number.equals(s.getNumber())) {
				if (s.getStat2() == 1) {
					System.out.println("Already Applied");
					return false;
				}
				continue;
			}
		}
		if (Status.final_application_check() == false)
		{
			System.out.println("It is not the period for final apply ");
			return false;
		}
		
				
		Scanner sc=new Scanner(System.in);
			System.out.println("Input quit to Quit(Otherwise press any key to continue)");
			quitOption=sc.nextLine();
			if(quitOption.equals("quit")) {
				Status.upload();
				return true;
			}
		
		for(Status b : Status.status)
			if(this.number.equals(b.getNumber())) {	// my number == b.getnumber , set step -> 2
				b.second_modify(1);					// after is up to the manager
				System.out.println(name + " : applied final apply !");
				break;
			}
		Status.upload();
		return true;
	}
	
	
	public boolean apply_transfercredits() { // apply for transfer credit
		String quitOption;
		boolean find_flag=false;
		if(Status.download() == false) {
			System.out.println("Status not found ");
			return false;
		}
		for(Status s: Status.status) {
			if (number.equals(s.getNumber())) {
				if (s.getStat3() == 1) {
					System.out.println("Already Applied");
					return false;
				}
				continue;
			}
		}
		if (Status.transfer_credit_application_check() == false)
		{
			System.out.println("It is not the period for transfer credit apply ");
			return false;
		}
		
	
		
	

		Scanner sc=new Scanner(System.in);
			System.out.println("Input quit to Quit(Otherwise press any key to continue)");
			quitOption=sc.next();
			if(quitOption.equals("quit")) {
				Status.upload();
				return true;
			}
		
	
		for(Status b : Status.status) {
			int count=0;
			if(this.number.equals(b.getNumber())) {	// my number == b.getnumber , set step -> 2
				for(Course c: b.getCourse()) {
					if(c.get_Score()!='F')
						count++;
					if(count>=4) {
						find_flag=true;
						break;
					}
				}
				if(find_flag) {
					b.final_modify(1);					// after is up to the manager	
					System.out.println(b.getName() + " : applied transfer_credit apply !");
				}
				else {
					System.out.println("Can't apply for transfer credit, Your Course_count(excluding F) is less than 4");
					b.final_modify(0);
				}
				break;
			}
		}
		
		Status.upload();
		return true;
	
	}

	public void sort_Dispatch() {
		int num;
		System.out.println("**********Input Sorting options**********");
		System.out.println("1. By college_name\t2. By period\t3. By major\n Input -1 to print without sorting");
		Scanner sc=new Scanner(System.in);
			num=sc.nextInt();
		
		switch(num) {
		case -1:
			return;
		case 1:
			Dispatch_Record.dispatch_record.sort((d1,d2)->d1.get_coll_name().compareTo(d2.get_coll_name()));
			break;
		case 2:
			Dispatch_Record.dispatch_record.sort((d1,d2)->d1.get_period().compareTo(d2.get_period()));
			break;
		case 3:
			Dispatch_Record.dispatch_record.sort((d1,d2)->d1.get_major().compareTo(d2.get_major()));
			break;
		}
	}
	
	public boolean see_dispatch_rec() { // see dispatch record
		String a;
		int count=0;
		Dispatch_Record.download();
		
		sort_Dispatch();
		Scanner sc = new Scanner(System.in);
			while (true) {
				System.out.println("\n**********Dispatch_Record**********");
				for(Dispatch_Record d : Dispatch_Record.dispatch_record)
					d.show_info();
				System.out.println();
				System.out.print("Input dispatch record period you want   ");
				System.out.println("ex) 2018_1");
				System.out.println("If you want to quit, Input quit");
				a = sc.next();
				if (a.equals("quit")) {
					System.out.println("'see dispatch record' quit");
					return true;
				} 
				else {
					System.out.println("\n**********Dispatch_Record for " + a + "**********");
					for (Dispatch_Record d : Dispatch_Record.dispatch_record)
					{
						if((d.get_period()).equals(a))
						{
							d.show_info();
							count++;
						}
					}	
					System.out.println();
					if(count==0)
						System.out.println("Input value is error");
					count=0;
					continue;
				}	
			}
		
	}
	public boolean major_apply() {
		
		Boolean find_flag1, find_flag2;
		String quitOption;
		String corNum;
		LinkedList<Course> course = new LinkedList<>();
		int count=-1;
		
		
		if (Status.download() == false) {
			System.out.println("Status not found ");
			return false;
		}
		if (Status.transfer_credit_application_check() == false) {
			System.out.println("It is not the period for transfer_credit apply ");

			return false;
		}

		Scanner sc = new Scanner(System.in);

		find_flag1 = false;
		System.out.println("If you want to quit, Input quit(Otherwise press any key to continue)");
		quitOption = sc.next();

		if (quitOption.equals("quit")) {
			Status.upload();
			return true;
		}

		for (Status s : Status.status) {
			if (number.equals(s.getNumber())) {
				find_flag1 = true;
				course = s.getCourse();
			} else
				continue;

			if (find_flag1) {
				
				while (true) {
					System.out.println("**********" + number + "'s course list**********");
					count=-1;
					for (Course c : course) {
						++count;
						if (c.get_major_stat() == false)
						{
							System.out.println( "\t" + count + "\t" +  c.getName());
							
						}
					}
					find_flag2 = false;
					System.out.println("Input course index to apply for major_change ");
					System.out.println("If you want to quit, Input quit");
					corNum = sc.next();

					if (corNum.equals("quit")) {
						System.out.println("'transfer credit course name' quit");
						s.setCourse(course);
						Status.upload();
						return true;
					}
					
					course.get(Integer.parseInt(corNum)).set_major_stat(true);
					s.setCourse(course);
					
					if (find_flag2)
						continue;
					System.out.println("input course name does not exist");
				}
			}
		}
		Status.upload();
		return false;

	}


	public boolean student_option()throws NoSuchElementException {
		int menu_option;
		Scanner sc = new Scanner(System.in);

		while (true) {
			for(int i=0;i<10;i++)
				System.out.println();
			System.out.println("**********Student Options**********");
			System.out.println("1. search for student's current status\n" + "2. print appliable Bulletin and apply\n"
					+ "3. register document\n" + "4. final application\n" + "5. apply for major course\n"
					+ "6. apply for transfer credit\n" + "7. canceling application\n" + "8. see dispatch record\n"
					+ "9. logout");
			while (true) {
				System.out.print("Insert option: ");
				menu_option = sc.nextInt();
				if (menu_option >= 1 && menu_option <= 9)
					break;
				System.out.println("Wrong input");
			}
			switch (menu_option) {
			case 1:
				see_cur_status();
				break;
			case 2:
				firstapply();
				break;
			case 3:
				register_document();
				break;
			case 4:
				finalapply();
				break;
			case 5:
				major_apply();
				break;
			case 6:
				apply_transfercredits();
				break;
			case 7:
				cancel_apply();
				break;
			case 8:
				see_dispatch_rec();
				break;
			case 9:
				return true;
			}
		}

	}

}