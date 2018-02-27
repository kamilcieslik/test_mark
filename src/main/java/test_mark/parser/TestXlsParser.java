package test_mark.parser;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import test_mark.answers_cards_collection.AnswersCard;
import test_mark.answers_cards_collection.AnswersCardsCollection;
import test_mark.answers_cards_collection.StudentQuestionAnswers;
import test_mark.exception.UniqueViolationException;
import test_mark.test_result.Statistics;
import test_mark.test_result.StudentResults;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestXlsParser {
    public TestXlsParser() {
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
                int column = 0;
                StudentQuestionAnswers studentQuestionAnswers = new StudentQuestionAnswers();
                while (studentQuestionsAnswersSheet.getRow(index).getCell(column) != null) {
                    if (column == 0) {
                        studentQuestionAnswers.setQuestionNumber((int) studentQuestionsAnswersSheet.getRow(index).getCell(column).getNumericCellValue());
                    } else {
                        if (studentQuestionsAnswersSheet.getRow(index).getCell(column).getStringCellValue().equals("-")) {
                            studentQuestionAnswers.addQuestionAnswer('-');
                            break;
                        }
                        studentQuestionAnswers.addQuestionAnswer(studentQuestionsAnswersSheet.getRow(index).getCell(column).getStringCellValue().charAt(0));
                    }
                    column++;
                }
                answersCard.addQuestionAnswers(studentQuestionAnswers);
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
            for (Map.Entry<Integer, StudentQuestionAnswers> studentQuestionsAnswers : answersCard.getValue().getStudentQuestionsAnswers().entrySet()) {
                HSSFRow studentAnswersSheetRow = studentAnswersSheet.createRow(index);
                studentAnswersSheetRow.createCell(0).setCellValue(studentQuestionsAnswers.getValue().getQuestionNumber());

                int i = 1;
                for (Character character : studentQuestionsAnswers.getValue().getQuestionAnswers()) {
                    studentAnswersSheetRow.createCell(i).setCellValue(Character.toString(character));
                    i++;
                }
                index++;
            }
        }

        File answersCardsCollectionFile = new File(directoryPath,
                generateAnswersCardsCollectionFilename("answers_cards_collection_", answersCardsCollection));
        FileOutputStream fileOutputStream = new FileOutputStream(answersCardsCollectionFile.getAbsolutePath());
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    public void writeExamResults(Statistics statistics, String directoryPath) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Wyniki");
        HSSFRow testInfoHeaderRow = sheet.createRow(0);
        testInfoHeaderRow.createCell(0).setCellValue("Kurs:");
        testInfoHeaderRow.createCell(1).setCellValue("Test wyboru:");
        testInfoHeaderRow.createCell(2).setCellValue("Data przeprowadzenia testu:");
        HSSFRow testInfoSheetRow = sheet.createRow(1);
        testInfoSheetRow.createCell(0).setCellValue(statistics.getAnswersCardsCollection().getCourseName());
        testInfoSheetRow.createCell(1).setCellValue(statistics.getAnswersCardsCollection().getExamName());
        testInfoSheetRow.createCell(2).setCellValue(new SimpleDateFormat("dd-MM-yyyy")
                .format(statistics.getAnswersCardsCollection().getExamDate()));

        HSSFRow resultsHeaderRow = sheet.createRow(4);
        resultsHeaderRow.createCell(0).setCellValue("Nr indeksu");
        resultsHeaderRow.createCell(1).setCellValue("Suma punktów");
        resultsHeaderRow.createCell(2).setCellValue("%");
        resultsHeaderRow.createCell(3).setCellValue("Ocena");

        int index = 5;
        for (StudentResults studentResults : statistics.getStudentsResults()) {
            HSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(studentResults.getIndex());
            row.createCell(1).setCellValue(studentResults.getNumberOfCorrectAnswers() +
                    "/" + statistics.getAnswersCardsCollection().getNumberOfQuestions());
            row.createCell(2).setCellValue(studentResults.getPercentageOfCorrectAnswers());
            row.createCell(3).setCellValue(studentResults.getTestMark());
            index++;
        }

        File answersCardsCollectionFile = new File(directoryPath,
                generateAnswersCardsCollectionFilename("exam_results_", statistics.getAnswersCardsCollection()));
        FileOutputStream fileOutputStream = new FileOutputStream(answersCardsCollectionFile.getAbsolutePath());
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    private String generateAnswersCardsCollectionFilename(String preName, AnswersCardsCollection answersCardsCollection) {
        StringBuilder sb = new StringBuilder(answersCardsCollection.getCourseName() + "_" + answersCardsCollection.getExamName());
        for (int index = 0; index < sb.length(); index++) {
            char c = sb.charAt(index);
            if (Character.isUpperCase(c))
                sb.setCharAt(index, Character.toLowerCase(c));
        }

        return preName + sb.toString().replaceAll(" ", "_") + "_"
                + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(answersCardsCollection.getExamDate()) + ".xls";
    }
}
