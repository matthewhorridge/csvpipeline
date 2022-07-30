package org.metadatacenter.csvpipeline;

import org.metadatacenter.csvpipeline.app.DataDictionaryProcessor;
import org.metadatacenter.csvpipeline.app.DataDictionaryRowProcessor;
import org.metadatacenter.csvpipeline.cedar.csv.CedarCsvParser;
import org.metadatacenter.csvpipeline.cli.CedarCsvCli;
import org.metadatacenter.csvpipeline.redcap.TemplateFieldWriter;
import org.metadatacenter.csvpipeline.ont.KnowledgeArtifactGenerator;
import org.metadatacenter.csvpipeline.ont.VocabularyWriter;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryParser;
import org.metadatacenter.csvpipeline.redcap.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class CsvPipelineApplication implements ApplicationRunner, ExitCodeGenerator {

	private String input = "";

	private String noHeader = "";

	private String outputDirectory = "";

	@Autowired
	private ApplicationContext applicationContext;

	private int exitCode;

	public CsvPipelineApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(CsvPipelineApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		var cli = applicationContext.getBean(CedarCsvCli.class);
		exitCode = cli.run(args.getSourceArgs());

//		var processor = applicationContext.getAutowireCapableBeanFactory()
//				.createBean(DataDictionaryProcessor.class);
//		var workingDirectory = Paths.get(".").toAbsolutePath();
//		var inputFile = workingDirectory.resolve(input);
//		var dataDictionaryInputStream = Files.newInputStream(inputFile);
//		processor.processDataDictionary(dataDictionaryInputStream);
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}

	@Bean
	Header header() {
		if(noHeader != null) {
			System.out.print("Processing file without header\n");
			return Header.WITHOUT_HEADER;
		}
		else {
			System.out.print("Processing file with header\n");
			return Header.WITH_HEADER;
		}
	}

	@Bean
	public DataDictionaryParser redCapCsvParser() {
		return new DataDictionaryParser();
	}

	@Bean
	DataDictionaryProcessor dataDictionaryProcessor(DataDictionaryParser dataDictionaryParser,
													Header header,
													DataDictionaryRowProcessor valuesProcessor) {
		return new DataDictionaryProcessor(header, dataDictionaryParser, valuesProcessor);
	}

	@Bean
	VocabularyWriter vocabularyWriter(KnowledgeArtifactGenerator knowledgeArtifactGenerator) {
		return new VocabularyWriter(knowledgeArtifactGenerator, Path.of(outputDirectory));
	}

	@Bean
	DataDictionaryRowProcessor dataDictionaryValuesProcessor(KnowledgeArtifactGenerator knowledgeArtifactGenerator,
															 TemplateFieldWriter templateFieldWriter,
															 VocabularyWriter vocabularyWriter) {
		return new DataDictionaryRowProcessor(knowledgeArtifactGenerator,
											  vocabularyWriter,
											  templateFieldWriter, outputDirectory);
	}
}
