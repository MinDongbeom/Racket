package com.multi.racket.inquiry;
import com.multi.racket.domain.InquiryDTO;
public interface InquiryService {
	public InquiryDTO insert(InquiryDTO dto);
	public InquiryDTO read(int inquiryNo);
	public InquiryDTO reply(InquiryDTO dto);
	public void delete(int inquiryNo);
}
