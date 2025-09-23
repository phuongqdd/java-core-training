package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizDetailResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizExportResponse;
import com.dophuong.lms.learning_management_system.enums.Difficulty;
import com.dophuong.lms.learning_management_system.service.QuizExportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizExportServiceImpl implements QuizExportService {

    private static final String UPLOAD_DIR = "/uploads/quiz/";

    @Override
    public QuizExportResponse exportQuizToWord(QuizDetailResponse quizDetail) {
        XWPFDocument document = new XWPFDocument();

        // --- Cấu hình trang A4 và lề ---
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();

        // Khổ giấy A4: 21cm x 29.7cm
        CTPageSz pageSize = sectPr.addNewPgSz();
        pageSize.setW(BigInteger.valueOf(11906)); // 21 cm = 11906 twips
        pageSize.setH(BigInteger.valueOf(16838)); // 29.7 cm = 16838 twips

        // Lề chuẩn
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setTop(BigInteger.valueOf(1134));    // 2 cm
        pageMar.setBottom(BigInteger.valueOf(1134)); // 2 cm
        pageMar.setLeft(BigInteger.valueOf(1701));   // 3 cm
        pageMar.setRight(BigInteger.valueOf(1134));  // 2 cm


        // --- Tiêu đề ---
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runTitle = title.createRun();
        runTitle.setText("ĐỀ KIỂM TRA");
        runTitle.setFontFamily("Times New Roman");
        runTitle.setFontSize(18);
        runTitle.setBold(true);

        // --- Thông tin phụ ---
        XWPFParagraph infoPara = document.createParagraph();
        infoPara.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runInfo = infoPara.createRun();
        runInfo.setFontFamily("Times New Roman");
        runInfo.setFontSize(12);
        runInfo.setText("Họ và tên: .............................................");
        runInfo.addBreak();
        runInfo.setText("Mã số sinh viên: ......................................");
        runInfo.addBreak();
        runInfo.setText("Thời gian làm bài: " + quizDetail.getTimeLimit() + " phút");
        runInfo.addBreak();
        runInfo.setText("Tổng số câu hỏi: " + quizDetail.getQuestionResponses().size());
        runInfo.addBreak();
        // --- Chỗ để giáo viên chấm điểm ---
        runInfo.setBold(true);
        runInfo.setText("Điểm: .......... / ..........");
        runInfo.addBreak();

        // --- Nội dung câu hỏi ---
        int index = 1;
        for (QuestionResponse response : quizDetail.getQuestionResponses()) {
            XWPFParagraph qParagraph = document.createParagraph();
            qParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun runQ = qParagraph.createRun();
            runQ.setBold(true);
            runQ.setFontFamily("Times New Roman");
            runQ.setFontSize(12);

            String level = switch (response.getDifficulty()) {
                case EASY -> "Nhận biết";
                case MEDIUM -> "Thông hiểu";
                case HARD -> "Vận dụng";
                case VERY_HARD -> "Vận dụng cao";
            };

            runQ.setText("Câu " + index++ + ". [" + level + "] " + response.getContent());

            // --- Các lựa chọn ---
            char optionChar = 'A';
            for (OptionResponse optionResponse : response.getOptions()) {
                XWPFParagraph oPara = document.createParagraph();
                oPara.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun runO = oPara.createRun();
                runO.setFontFamily("Times New Roman");
                runO.setFontSize(12);
                runO.setText(optionChar + ". " + optionResponse.getContent());
                optionChar++;
            }

        }

        XWPFParagraph endPara = document.createParagraph();
        endPara.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runEnd = endPara.createRun();
        runEnd.setFontFamily("Times New Roman");
        runEnd.setFontSize(12);
        runEnd.setBold(true);
        runEnd.setText("===HẾT===");

        // --- Phần đáp án ---
        XWPFParagraph answerTitle = document.createParagraph();
        answerTitle.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runAnswerTitle = answerTitle.createRun();
        runAnswerTitle.setText("ĐÁP ÁN");
        runAnswerTitle.setFontFamily("Times New Roman");
        runAnswerTitle.setFontSize(16);
        runAnswerTitle.setBold(true);
        runAnswerTitle.addBreak();

        // --- Tạo bảng đáp án ---
        int maxColumnsPerRow = 8; // tối đa 4 câu mỗi hàng
        XWPFTable table = document.createTable();

        XWPFTableRow currentRow = table.getRow(0); // hàng đầu tiên
        int cellIndex = 0; // ô hiện tại trong hàng

        int ansIndex = 1;
        for (QuestionResponse response : quizDetail.getQuestionResponses()) {
            // Lấy đáp án đúng
            String correctOption = "";
            char optionChar = 'A';
            for (OptionResponse option : response.getOptions()) {
                if (option.isCorrect()) {
                    correctOption = String.valueOf(optionChar);
                    break;
                }
                optionChar++;
            }

            // Nếu ô hiện tại >= maxColumns → tạo hàng mới
            if (cellIndex >= maxColumnsPerRow) {
                currentRow = table.createRow();
                cellIndex = 0;
            }

            // Tạo hoặc lấy ô hiện tại
            XWPFTableCell cell = currentRow.getCell(cellIndex);
            if (cell == null) {
                cell = currentRow.createCell();
            }
            cell.setText("Câu " + ansIndex++ + ". " + correctOption);

            cellIndex++;
        }

        // --- Chân trang: đánh số trang ---
        XWPFHeaderFooterPolicy footerPolicy = document.createHeaderFooterPolicy();
        XWPFFooter footer = footerPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
        XWPFParagraph footerPara = footer.getParagraphArray(0);
        if (footerPara == null) footerPara = footer.createParagraph();
        footerPara.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runFooter = footerPara.createRun();
        runFooter.setFontFamily("Times New Roman");
        runFooter.setFontSize(12);
        runFooter.setText("Trang ");
        footerPara.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT"); // đánh số trang

        File dir = new File(UPLOAD_DIR);
        if(!dir.exists())
            dir.mkdirs();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "Đề và đáp án chấm-" + quizDetail.getId() + "-" + timestamp + ".docx";
        String filePath = UPLOAD_DIR + fileName;

        try (FileOutputStream out = new FileOutputStream(filePath)){
            document.write(out);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return QuizExportResponse.builder()
                .fileName(fileName)
                .filePath(filePath)
                .build();
    }
}
