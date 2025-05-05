import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {
    private SentimentResult[] Results;
    private final SentimentAnalyzer analyzer = new SentimentAnalyzer();
    private final JFileChooser FileChooser = new JFileChooser();
    private final DefaultListModel<File> FileListModel;
    private final JList<File> FileList;
    private JComboBox<SentimentScorer> ScorerDropdown;

    public MainFrame() {
        this.setSize(500, 500);
        this.setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FileFilter filter = new FileNameExtensionFilter("txt", "txt");
        FileChooser.setFileFilter(filter);

        JLabel label = new JLabel("Files");
        label.setBounds(150, 30, 50, 20);

        FileListModel = new DefaultListModel<>();
        FileList = new JList<>(FileListModel);
        FileList.setCellRenderer(new FileCellRenderer());
        FileList.setBounds(50, 50, 200, 100);

        this.add(label);
        this.add(FileList);

        JPanel MenuPanel = new JPanel();
        JFrame FileChooserFrame = new JFrame();
        FileChooserFrame.setSize(500, 500);

        MenuPanel.setLayout(new BoxLayout(MenuPanel, BoxLayout.PAGE_AXIS));
        MenuPanel.setBounds(270, 90, 200, 300);

        Container DictionarySettingComponent = CreateDictionarySettingComponent(FileChooserFrame);
        Container FileSaveSettings = CreateFileSaveSettings(FileChooserFrame);

        JButton FileUploadButton = CreateFileUploadButton(FileChooserFrame);
        JButton RemoveFilesButton = CreateRemoveFilesButton();

        Container ScorerDropdownContainer = CreateScorerDropdownContainer();
        JButton AnalyzerButton = CreateAnalyzerButton();

        Component[] buttons = {
                DictionarySettingComponent,
                FileUploadButton,
                AnalyzerButton,
                RemoveFilesButton,
                FileSaveSettings,
                ScorerDropdownContainer
        };

        for (Component button : buttons) {
            MenuPanel.add(button);
            MenuPanel.add(Box.createVerticalStrut(10));
        }

        MenuPanel.setVisible(true);
        this.add(MenuPanel);

        this.setVisible(true);
    }

    private Container CreateScorerDropdownContainer() {
        SentimentScorer[] Scorers = { new RatioScorer(), new WeightedScorer() };
        ScorerDropdown = new JComboBox<>(Scorers);
        JLabel ScorerDropdownLabel = new JLabel("Scorers");

        return CreateContainerWithSpaceSeperatedElements(ScorerDropdownLabel, ScorerDropdown);
    }

    private JButton CreateAnalyzerButton() {
        JButton AnalyzerButton = new JButton("Analyze Sentiments");
        JTextArea OutputLabel = new JTextArea();

        JScrollPane scrollPane = new JScrollPane(OutputLabel);
        scrollPane.setBounds(50, 180, 200, 200);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane);

        AnalyzerButton.addActionListener((Event) -> {
            SentimentScorer Scorer = (SentimentScorer) ScorerDropdown.getSelectedItem();

            Object[] fileObjects = FileListModel.toArray();
            File[] files = new File[fileObjects.length];

            for (int i = 0;i < fileObjects.length;i++) {
                files[i] = (File) fileObjects[i];
            }

            try {
                Results = analyzer.BatchAnalyzeFromDict(files, Scorer);

                StringBuilder OverallReport = new StringBuilder();
                for (SentimentResult result : Results) {
                    OverallReport.append(result.getSummary());
                }

                OutputLabel.setText(OverallReport.toString());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return AnalyzerButton;
    }

    private Container CreateDictionarySettingComponent(JFrame FileChooserFrame) {
        JButton DictionaryUploadButton = new JButton("Load Sentiment Dictionary");
        JLabel CurrentDictionaryLabel = new JLabel("Not Selected");
        Container DictionarySettingComponent = CreateContainerWithSpaceSeperatedElements(DictionaryUploadButton, CurrentDictionaryLabel);

        DictionaryUploadButton.addActionListener((Event) -> {
            FileChooser.setMultiSelectionEnabled(false);
            FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileChooser.setSelectedFile(new File(""));

            FileChooser.setCurrentDirectory(new File("C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\dictionary"));
            int FileChooserReturn = FileChooser.showOpenDialog(FileChooserFrame);
            switch (FileChooserReturn) {
                case JFileChooser.APPROVE_OPTION -> {
                    File SentimentDictionaryFile = FileChooser.getSelectedFile();

                    // TODO: file existence

                    try {
                        analyzer.LoadDictionary(SentimentDictionaryFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    CurrentDictionaryLabel.setText(SentimentDictionaryFile.getName());
                }
                case JFileChooser.CANCEL_OPTION -> {
                    CurrentDictionaryLabel.setText("Dictionary not Set");
                }
            }
        });

        return DictionarySettingComponent;
    }

    private JButton CreateRemoveFilesButton() {
        JButton RemoveFilesButton = new JButton("Remove Files");

        RemoveFilesButton.addActionListener((Event) -> {
            List<File> SelectedFiles = FileList.getSelectedValuesList();

            for (File file : SelectedFiles) {
                FileListModel.removeElement(file);
            }
        });

        return RemoveFilesButton;
    }

    private Container CreateFileSaveSettings(JFrame FileChooserFrame) {
        JButton SaveReportButton = new JButton("Save Report");
        JLabel SaveLabel = new JLabel();
        Container FileSaveSettings = CreateContainerWithSpaceSeperatedElements(SaveReportButton, SaveLabel);

        SaveReportButton.addActionListener((Event) -> {
            if (Results == null) {
                SaveLabel.setText("No available results!");
                return;
            }

            FileChooser.setMultiSelectionEnabled(false);
            FileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            FileChooser.setSelectedFile(new File(""));

            FileChooser.setCurrentDirectory(new File("C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\input"));
            int FileChooserReturn = FileChooser.showSaveDialog(FileChooserFrame);

            if (FileChooserReturn == JFileChooser.APPROVE_OPTION) {
                File directory = FileChooser.getSelectedFile();

                String fileName = createTimestampedFileName("report-%s.txt", "yyyyMMdd-hhMMss");
                File file = Paths.get(directory.getPath(), fileName).toFile();

                try (PrintWriter writer = new PrintWriter(file)) {

                    for (SentimentResult result : Results) {
                        writer.write(result.getSummary());
                        writer.write("\n");
                    }

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return FileSaveSettings;
    }

    private JButton CreateFileUploadButton(JFrame FileChooserFrame) {
        JButton FileUploadButton = new JButton("Load Files");

        FileUploadButton.addActionListener((Event) -> {
            FileChooser.setMultiSelectionEnabled(true);
            FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileChooser.setSelectedFile(new File(""));

            FileChooser.setCurrentDirectory(new File("C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\input"));
            int FileChooserReturn = FileChooser.showOpenDialog(FileChooserFrame);

            switch (FileChooserReturn) {
                case JFileChooser.APPROVE_OPTION -> {
                    File[] SelectedFiles = FileChooser.getSelectedFiles();

                    for (File file : SelectedFiles) {
                        FileListModel.addElement(file);
                    }
                }
            }
        });

        return FileUploadButton;
    }

    public Container CreateContainerWithSpaceSeperatedElements(Component ...Components) {
        Container container = new Container();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        for (Component component : Components) {
            container.add(component);
            container.add(Box.createVerticalStrut(5));
        }

        return container;
    }

    public static String createTimestampedFileName(String fileName, String timestampFormat) {
        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampFormat);
        String timestamp = currentTime.format(formatter);

        return String.format(fileName, timestamp);
    }
}
