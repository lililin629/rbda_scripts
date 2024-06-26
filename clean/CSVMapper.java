import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import java.io.IOException;
import java.util.Scanner;
import org.apache.hadoop.io.NullWritable;

// public class CSVMapper extends Mapper<Object, Text, NullWritable, Text> {

//     private Text outputValue = new Text();

//     public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
//         // Use Scanner to read the line using a comma delimiter while respecting quotes
//         Scanner scanner = new Scanner(value.toString());
//         scanner.useDelimiter(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//         try {
//             if (scanner.hasNext()) {
//                 String movieName = scanner.next().trim();
//                 String movieDate = scanner.next().trim();
//                 scanner.next(); // Skip 'Serie Name'
//                 scanner.next(); // Skip 'Serie Date'
//                 String movieType = scanner.next().trim();
//                 scanner.next(); // Skip 'Number of Votes'
//                 String movieRevenue = scanner.next().trim();
//                 String score = scanner.next().trim();
//                 String metascore = scanner.next().trim();
//                 scanner.next(); // Skip 'Time Duration'
//                 String director = scanner.next().trim();

//                 // Construct the output value without the movie name as key
//                 String output = movieName + "," + movieDate + "," + movieType + "," + movieRevenue + "," + score + ","
//                         + metascore + "," + director;
//                 outputValue.set(output);

//                 // Emit with null key
//                 context.write(NullWritable.get(), outputValue);
//             }
//         } finally {
//             scanner.close();
//         }
//     }
// }

// import org.apache.hadoop.io.NullWritable;
// import java.io.IOException;
// import java.util.Scanner;

// public class CSVMapper extends Mapper<Object, Text, NullWritable, Text> {

//     private Text outputValue = new Text();

//     public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
//         // Use Scanner to read the line using a comma delimiter while respecting quotes
//         Scanner scanner = new Scanner(value.toString());
//         scanner.useDelimiter(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//         try {
//             if (scanner.hasNext()) {
//                 String movieName = scanner.next().trim();
//                 String movieDate = scanner.next().trim();
//                 scanner.next(); // Skip 'Serie Name'
//                 scanner.next(); // Skip 'Serie Date'
//                 String movieType = scanner.next().trim();
//                 scanner.next(); // Skip 'Number of Votes'
//                 String movieRevenue = scanner.next().trim();
//                 String score = scanner.next().trim();
//                 String metascore = scanner.next().trim();
//                 scanner.next(); // Skip 'Time Duration'
//                 String director = scanner.next().trim();

//                 // Removing parentheses from movieDate
//                 movieDate = movieDate.replaceAll("[()]", ""); // Regex to remove any parentheses

//                 // Construct the output value without the movie name as key
//                 String output = movieName + "," + movieDate + "," + movieType + "," + movieRevenue + "," + score + ","
//                         + metascore + "," + director;
//                 outputValue.set(output);

//                 // Emit with null key
//                 context.write(NullWritable.get(), outputValue);
//             }
//         } finally {
//             scanner.close();
//         }
//     }
// }

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVMapper extends Mapper<Object, Text, NullWritable, Text> {

    private Text outputValue = new Text();
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{4})"); // Pattern to extract the first four digits

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Use Scanner to read the line using a comma delimiter while respecting quotes
        Scanner scanner = new Scanner(value.toString());
        scanner.useDelimiter(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        try {
            if (scanner.hasNext()) {
                String movieName = scanner.next().trim();
                String movieDate = extractYear(scanner.next().trim()); // Extract only the year
                scanner.next(); // Skip 'Serie Name'
                scanner.next(); // Skip 'Serie Date'
                String movieType = scanner.next().trim();
                scanner.next(); // Skip 'Number of Votes'
                String movieRevenue = scanner.next().trim();
                String score = scanner.next().trim();
                String metascore = scanner.next().trim();
                scanner.next(); // Skip 'Time Duration'
                String director = scanner.next().trim();

                // Construct the output value
                String output = String.join(",", movieName, movieDate, movieType, movieRevenue, score, metascore,
                        director);
                outputValue.set(output);

                // Emit with null key
                context.write(NullWritable.get(), outputValue);
            }
        } finally {
            scanner.close();
        }
    }

    private String extractYear(String movieDate) {
        Matcher matcher = YEAR_PATTERN.matcher(movieDate);
        return matcher.find() ? matcher.group(1) : "Unknown"; // Return the first match or "Unknown" if no match found
    }
}
