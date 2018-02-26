package test_mark.parsers;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import test_mark.answers_cards_collection.AnswersCard;
import test_mark.answers_cards_collection.AnswersCardsCollection;
import test_mark.exception.UniqueViolationException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnswersCardCollectionXlsParser {
    public AnswersCardCollectionXlsParser() {
    }

    public AnswersCardsCollection readAnswersCardsCollection(File answersCardsCollectionFile) throws UniqueViolationException, IOException {
        InputStream input = new FileInputStream(answersCardsCollectionFile);
        POIFSFileSystem fs = new POIFSFileSystem(input);
        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFSheet testInformation = wb.getSheetAt(0);
        AnswersCardsCollection answersCardsCollection = new AnswersCardsCollection();
        answersCardsCollection.setCourseName(testInformation.getRow(0).getCell(0).getStringCellValue());
        answersCardsCollection.setExamName(testInformation.getRow(0).getCell(1).getStringCellValue());
        answersCardsCollection.setExamDate(testInformation.getRow(0).getCell(2).getDateCellValue());
        answersCardsCollection.setNumberOfQuestions((int) testInformation.getRow(0).getCell(3).getNumericCellValue());

        for (int i = 1; i < wb.getNumberOfSheets(); i++) {
            HSSFSheet studentQuestionsAnswersSheet = wb.getSheetAt(i);
            AnswersCard answersCard = new AnswersCard();
            answersCard.setStudentIndex(wb.getSheetName(i));

            int index = 0;
            while (studentQuestionsAnswersSheet.getRow(index) != null) {
                System.out.println("ELO");
                int column = 0;
                List<Character> questionAnswersList = new ArrayList<>();
                while (studentQuestionsAnswersSheet.getRow(index).getCell(column) != null) {
                    System.out.println("ELO2");
                    if (studentQuestionsAnswersSheet.getRow(index).getCell(column).getStringCellValue().equals("-")) {
                        questionAnswersList.add('-');
                        break;
                    }
                    questionAnswersList.add(studentQuestionsAnswersSheet.getRow(index).getCell(column).getStringCellValue().charAt(0));
                    column++;
                }
                answersCard.addQuestionAnswers(questionAnswersList);
                index++;
            }
            answersCardsCollection.addAnswersCard(answersCard);
        }

        return answersCardsCollection;
    }

    public void writeAnswersCardsCollection(AnswersCardsCollection answersCardsCollection, String directoryPath) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet testInfoSheet = workbook.createSheet("Info");
        HSSFRow testInfoSheetRow = testInfoSheet.createRow(0);
        testInfoSheetRow.createCell(0).setCellValue(answersCardsCollection.getCourseName());
        testInfoSheetRow.createCell(1).setCellValue(answersCardsCollection.getExamName());
        testInfoSheetRow.createCell(2).setCellValue(answersCardsCollection.getExamDate());
        testInfoSheetRow.createCell(3).setCellValue(answersCardsCollection.getNumberOfQuestions());

        for (Map.Entry<String, AnswersCard> answersCard : answersCardsCollection.getAnswersCards().entrySet()) {
            HSSFSheet studentAnswersSheet = workbook.createSheet(answersCard.getKey());
            int index = 0;
            for (List<Character> studentQuestionsAnswers : answersCard.getValue().getStudentQuestionsAnswers()) {
                HSSFRow studentAnswersSheetRow = studentAnswersSheet.createRow(index);
                for (int i = 0; i < studentQuestionsAnswers.size(); i++)
                    studentAnswersSheetRow.createCell(i).setCellValue(Character.toString(studentQuestionsAnswers.get(i)));
                index++;
            }
        }

        File answersCardsCollectionFile = new File(directoryPath, generateAnswersCardsCollectionFilename(answersCardsCollection));
        FileOutputStream fileOutputStream = new FileOutputStream(answersCardsCollectionFile.getAbsolutePath());
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    private String generateAnswersCardsCollectionFilename(AnswersCardsCollection answersCardsCollection) {
        StringBuilder sb = new StringBuilder(answersCardsCollection.getCourseName() + "_" + answersCardsCollection.getExamName());
        for (int index = 0; index < sb.length(); index++) {
            char c = sb.charAt(index);
            if (Character.isUpperCase(c))
                sb.setCharAt(index, Character.toLowerCase(c));
        }

        return "answers_cards_collection_" + sb.toString().replaceAll(" ", "_") + "_"
                + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(answersCardsCollection.getExamDate()) + ".xls";
    }
}
