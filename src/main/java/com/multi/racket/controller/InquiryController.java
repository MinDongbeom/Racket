package com.multi.racket.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.racket.domain.CashDTO;
import com.multi.racket.domain.InquiryDTO;
import com.multi.racket.domain.MemberDTO;
import com.multi.racket.inquiry.InquiryService;
@Controller
public class InquiryController {
	InquiryService service;
	
	@Autowired
	public InquiryController(InquiryService service) {
		super();
		this.service = service;
	}
	
	@RequestMapping("/inquiryread")
	public String inqread(int inquiryNo, Model model, HttpSession session) {
		InquiryDTO inquiry = service.read(inquiryNo);
		model.addAttribute("inquiry", inquiry);	
		MemberDTO member = (MemberDTO) session.getAttribute("user");
		model.addAttribute("member", member);	
		return "thymeleaf/inq/inquiryread";
	}
	
	@GetMapping("/inquirywrite")
	public String inqwrite(Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
		MemberDTO member = (MemberDTO)session.getAttribute("user");
		model.addAttribute("member",member);
		return "thymeleaf/inq/inquirywrite";
	}
	
	@PostMapping("/inquiry_write")
	public String write(InquiryDTO inquiry) {
		service.insert(inquiry);
		return "redirect:/inquiryboard?pageNo=0";
	}
	
	@GetMapping("/inquiry/updatepage")
	public String inqupdatepage(int inquiryNo,Model model) {
		InquiryDTO inquiry = service.read(inquiryNo);
		model.addAttribute("inquiry", inquiry);
		return "thymeleaf/inq/inquiryupdate";
	}
	
	@PostMapping("/inquiry/update")
	public String inqupdate(InquiryDTO updatedata) {
		service.update(updatedata);
		return "redirect:/inquiryread?inquiryNo="+updatedata.getInquiryNo()+"&state=READ";
	}
	
	@PostMapping("/inquiry/reply")
	public String inqreply(InquiryDTO reply, HttpSession session) {
		service.reply(reply);
		return "redirect:/inquiryread?inquiryNo="+reply.getInquiryNo()+"&state=READ";
	}
	
	@GetMapping("/inquiry/delete")
	public String inqdelete(int inquiryNo) {
		service.delete(inquiryNo);
		return "redirect:/inquiryboard?pageNo=0";
	}
	
	@RequestMapping("/report")
	public String report() {
		return "thymeleaf/inq/report";
	}
	
	@RequestMapping("/cashpay")
	public String pay() {
		return "thymeleaf/mypage/paychoice";
	}
	
	@RequestMapping("/cashchoice")
	public String pay2(Model model, HttpSession session) {
		MemberDTO member = (MemberDTO)session.getAttribute("user");
		model.addAttribute("member",member);
		return "thymeleaf/mypage/cashchoice";
	}
	
	@RequestMapping(value = "/paycomplete",
			produces = "application/text;charset=utf-8")
	@ResponseBody
	public String paycomplete(@RequestParam("won") int won,HttpServletRequest request) {
		HttpSession session = request.getSession();
		MemberDTO member = (MemberDTO)session.getAttribute("user");
		String id = member.getMemberId();
		//int updatecash = member.getTotalAmount();
		MemberDTO updatemember = service.updatecash(id, won);
		session.setAttribute("user", updatemember);	
		MemberDTO member2 = (MemberDTO)session.getAttribute("user");	
		CashDTO cash = new CashDTO(member.getMemberId(),0,member2.getTotalAmount(),won);
		service.insert(cash);
		return "test";
	}
	
					
}
