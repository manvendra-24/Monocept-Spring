package com.techlabs.app.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfGeneratorService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    public String generateStatement(String accountNumber, String pdfPath) throws DocumentException {
    	
    	String filename = pdfPath + "/ACC" + accountNumber + ".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            
            Account accountx = accountRepository.findById(Integer.parseInt(accountNumber)).get();
            //Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            document.add(new Paragraph("ACCOUNT STATEMENT"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Account Number : " + accountNumber));
            document.add(new Paragraph("Account Holder Name: " + accountx.getCustomer().getFirstname() + " " + accountx.getCustomer().getLastname()));
            document.add(new Paragraph("Account Balance: " + 1000));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            PdfPTable table = new PdfPTable(5);
            
            
            table.setWidths(new int[] {1,1,1,2,1});
            
            Stream.of("Date","Time", "Amount", "Details","Balance")
            		.forEach(columnTitle -> {
            		PdfPCell header = new PdfPCell();
            		header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            		header.setBorderWidth(2);
            		header.setPhrase(new Phrase(columnTitle));
            		header.getWidth();
            		table.addCell(header);
            		}
            );


            
            int number = Integer.parseInt(accountNumber);
            Account account = accountRepository.findById(number).get();
            
            
            List<Transaction> transactions = transactionRepository.findBySenderAccountOrReceiverAccount(account, number);
            
            
            for (Transaction transaction : transactions) {
            	String detail = transaction.getType();
            	int balance = transaction.getSenderBalance();
            	if(transaction.getType().equals("Transfer")) {
            		if(number == transaction.getSenderAccount().getAccountNumber()) {
                		detail += "red to: " + transaction.getReceiverAccount();
                		balance = transaction.getSenderBalance();
            		}
            		else if(number == transaction.getReceiverAccount()){
            			detail += "red from: " + transaction.getSenderAccount().getAccountNumber();
            			balance = transaction.getReceiverBalance();
            		}
            	}

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                String date = transaction.getDate().format(dateFormatter);
                String time = transaction.getDate().format(timeFormatter);
                
                table.addCell(date);
                table.addCell(time);
                table.addCell(Integer.toString(transaction.getAmount()));
                table.addCell(detail);
                table.addCell(Integer.toString(balance));
            }

            document.add(table);

            document.close();

            System.out.println("PDF created successfully at " + pdfPath);
            
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return filename;
    }



}
