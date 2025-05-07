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
    /// Instance Fields
    private SentimentResult[] Results;
    private final SentimentAnalyzer analyzer = new SentimentAnalyzer();
    private final JFileChooser FileChooser = new JFileChooser();

    public MainFrame() {
        // Basic configuration setup
        this.setSize(500, 500);
        this.setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // FileFilter for the FileChooser
        FileFilter filter = new FileNameExtensionFilter("txt", "txt");
        FileChooser.setFileFilter(filter);

        /// File Catalog displaying the list of currently added files and selected files

        // Label header for File Catalog
        JLabel label = new JLabel("Files");
        label.setBounds(150, 30, 50, 20);

        // Constructing a JList and FileListModel for storing and displaying files
        DefaultListModel<File> FileListModel = new DefaultListModel<>();
        JList<File> FileList = new JList<>(FileListModel);

        // Adding Custom Renderer for Files
        FileList.setCellRenderer(new FileCellRenderer());
        FileList.setBounds(50, 50, 200, 100);

        // Adding to this frame
        this.add(label);
        this.add(FileList);

        // Defining the window for the file chooser
        JFrame FileChooserFrame = new JFrame();
        FileChooserFrame.setSize(500, 500);

        /// Creating a panel of all the buttons

        JPanel MenuPanel = new JPanel();
        MenuPanel.setLayout(new BoxLayout(MenuPanel, BoxLayout.PAGE_AXIS));
        MenuPanel.setBounds(270, 90, 200, 300);

        // Adding the dictionary components
        Container DictionarySettingComponent = CreateDictionarySettingComponent(FileChooserFrame);

        // Adding the file save components
        Container FileSaveSettings = CreateFileSaveSettings(FileChooserFrame);

        // File uploading and file removal which requires access to the FileListModel
        JButton FileUploadButton = CreateFileUploadButton(FileChooserFrame, FileListModel);
        JButton RemoveFilesButton = CreateRemoveFilesButton(FileListModel, FileList);

        // A dropdown containing all the available SentimentScorers
        SentimentScorer[] Scorers = { new RatioScorer(), new WeightedScorer(), new EntropyScorer(), new WeightedAverageScorer() };
        JComboBox<SentimentScorer> ScorerComboBox = new JComboBox<>(Scorers);

        // Adding the header for the dropdown
        JLabel ScorerDropdownLabel = new JLabel("Scorers");
        Container ScorerDropdown = CreateContainerWithSpaceSeperatedElements(ScorerDropdownLabel, ScorerComboBox);

        // Output label displaying the results of the analysis
        JTextArea OutputLabel = CreateOutputLabel();

        // Analyzer Button
        JButton AnalyzerButton = CreateAnalyzerButton(FileListModel, FileList, OutputLabel, ScorerComboBox);

        // Adding components to this frame with 10unit spacing
        Component[] buttons = {
                DictionarySettingComponent,
                FileUploadButton,
                AnalyzerButton,
                RemoveFilesButton,
                FileSaveSettings,
                ScorerDropdown
        };

        for (Component button : buttons) {
            MenuPanel.add(button);
            MenuPanel.add(Box.createVerticalStrut(10));
        }

        MenuPanel.setVisible(true);
        this.add(MenuPanel);
    }
    
    private JTextArea CreateOutputLabel() {
        JTextArea OutputLabel = new JTextArea();

        // Scroll pane to make the JTextArea scrollable horizontally and vertically
        JScrollPane scrollPane = new JScrollPane(OutputLabel);
        scrollPane.setBounds(50, 180, 200, 200);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane);

        return OutputLabel;
    }

    /**
     * Creates the Analyzer Button and its logic
     */
    private JButton CreateAnalyzerButton(DefaultListModel<File> FileListModel, JList<File> FileList, JTextArea OutputLabel, JComboBox<SentimentScorer> ScorerDropdown) {
        JButton AnalyzerButton = new JButton("Analyze Sentiments");

        // Event listener for when the user presses this button
        AnalyzerButton.addActionListener((Event) -> {
            // Get the scorer from currently selected from the dropdown
            SentimentScorer Scorer = (SentimentScorer) ScorerDropdown.getSelectedItem();

            File[] files;

            // If there are no selected items in the file catalog
            // default to analyzing them all
            // else analyze the ones currently being selected
            if (FileList.getSelectedValuesList().isEmpty()) {
                // Casting each element from the .toArray() from Object to String
                // since casting Object[] to String[] is insufficient
                Object[] fileObjects = FileListModel.toArray();
                files = new File[fileObjects.length];

                for (int i = 0; i < fileObjects.length; i++) {
                    files[i] = (File) fileObjects[i];
                }

            } else {
                files = FileList.getSelectedValuesList().toArray(new File[0]);
            }

            try {
                // Analyze the batch of files and append the batch report
                // to the output lable

                Results = analyzer.BatchAnalyzeFromDict(files, Scorer);
                String report = SentimentAnalyzer.WriteBatchReport(Results);
                OutputLabel.setText(report);

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

        // Event listener for when the dictionary upload button is pressed
        DictionaryUploadButton.addActionListener((Event) -> {
            // Initializing FileChooser for uploading the dictionary
            FileChooser.setMultiSelectionEnabled(false);
            FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileChooser.setSelectedFile(new File(""));

//            FileChooser.setCurrentDirectory(new File("C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\dictionary"));
            int FileChooserReturn = FileChooser.showOpenDialog(FileChooserFrame);

            // Handling different return results of the FileChooser.showOpenDialog
            switch (FileChooserReturn) {
                case JFileChooser.APPROVE_OPTION -> {
                    File SentimentDictionaryFile = FileChooser.getSelectedFile();

                    // If The file exists load the analyzer's dictionary

                    try {
                        analyzer.LoadDictionary(SentimentDictionaryFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    // Set the label to the current dictionary label

                    CurrentDictionaryLabel.setText(SentimentDictionaryFile.getName());
                }
                case JFileChooser.CANCEL_OPTION -> {
                    CurrentDictionaryLabel.setText("Dictionary not Set");
                }
            }
        });

        return DictionarySettingComponent;
    }

    private JButton CreateRemoveFilesButton(DefaultListModel<File> FileListModel, JList<File> FileList) {
        JButton RemoveFilesButton = new JButton("Remove Files");

        RemoveFilesButton.addActionListener((Event) -> {
            List<File> SelectedFiles = FileList.getSelectedValuesList();

            // Get the files currently selected in the files catalog and remove them

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

            // Initialize the FileChooser for FileSave
            FileChooser.setMultiSelectionEnabled(false);
            FileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            FileChooser.setSelectedFile(new File(""));

//            FileChooser.setCurrentDirectory(new File("C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\input"));
            int FileChooserReturn = FileChooser.showSaveDialog(FileChooserFrame);

            if (FileChooserReturn == JFileChooser.APPROVE_OPTION) {
                File directory = FileChooser.getSelectedFile();

                // Create the file to which the report will be written to
                String fileName = Main.createTimestampedFileName("report-%s.txt", "yyyyMMdd-hhMMss");
                File file = Paths.get(directory.getPath(), fileName).toFile();

                // Write the results using PrintWriter
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.write(SentimentAnalyzer.WriteBatchReport(Results));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return FileSaveSettings;
    }

    private JButton CreateFileUploadButton(JFrame FileChooserFrame, DefaultListModel<File> FileListModel) {
        JButton FileUploadButton = new JButton("Load Files");

        // Event listener for when the file upload button is pressed
        FileUploadButton.addActionListener((Event) -> {
            // Initialize FileChooser for this button
            FileChooser.setMultiSelectionEnabled(true);
            FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileChooser.setSelectedFile(new File(""));

//            FileChooser.setCurrentDirectory(new File("C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\input"));
            int FileChooserReturn = FileChooser.showOpenDialog(FileChooserFrame);

            switch (FileChooserReturn) {
                case JFileChooser.APPROVE_OPTION -> {
                    File[] SelectedFiles = FileChooser.getSelectedFiles();

                    // Add all selected files in teh file chooser to the Files Catalog
                    for (File file : SelectedFiles) {
                        FileListModel.addElement(file);
                    }
                }
            }
        });

        return FileUploadButton;
    }

    /**
     * Helper Method to create a container with a list of components with vertical spacing
     *
     * @param Components the List of components
     *
     * @return Container with the space-seperated components
     */
    public Container CreateContainerWithSpaceSeperatedElements(Component ...Components) {
        // Create the container and use a BoxLayout to get vertical positioning along the page axis
        Container container = new Container();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        for (Component component : Components) {
            container.add(component);
            container.add(Box.createVerticalStrut(5));
        }

        return container;
    }
}
