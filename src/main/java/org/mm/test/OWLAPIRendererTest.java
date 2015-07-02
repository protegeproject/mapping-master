package org.mm.test;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.owlapi.OWLAPIRenderer;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class OWLAPIRendererTest
{
  public static void main(String args[])
  {

    try {
      String expressionText = "Class: @A3";
      MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expressionText.getBytes()));
      OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = ontologyManager.createOntology();
      File file = File.createTempFile("temp", "xlsx");
      WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
      WritableSheet sheet = writableWorkbook.createSheet("s1", 0);
      Label label0=new Label(0,0, "Derw");
      sheet.addCell(label0);
      writableWorkbook.write();
      writableWorkbook.close();

      Workbook workbook = Workbook.getWorkbook(file);

      SpreadSheetDataSource dataSource = new SpreadSheetDataSource(workbook);

      OWLAPIRenderer renderer = new OWLAPIRenderer(ontology, dataSource);

      SimpleNode expressionNode = parser.expression();
      ExpressionNode expression = new ExpressionNode((ASTExpression)expressionNode);

      expressionNode.dump(" ");

      System.err.println("expression.toString()      : " + expression.toString());
      System.err.println("renderer.render(expression): " + renderer.renderExpression(expression));

    } catch (MappingMasterException | ParseException | OWLOntologyCreationException | IOException | jxl.read.biff.BiffException | WriteException  e) {
      System.err.println("Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void Usage()
  {
    System.err.println("Usage: OWLAPIRendererTest <Expression>");
    System.exit(-1);
  }
}