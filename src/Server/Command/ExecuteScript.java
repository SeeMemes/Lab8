package Server.Command;

import Server.NewServer.CommandShell;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.Converter;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class ExecuteScript extends Command {
    public ExecuteScript(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        CommandShell commandShell = new CommandShell();
        try{
            StringTokenizer tokenizer = new StringTokenizer(command);
            String scriptPath;
            do {
                scriptPath = tokenizer.nextToken();
            } while (tokenizer.hasMoreTokens());

            InputStream inputStream = new FileInputStream(scriptPath);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready() || b) {
                String cmd;
                if (bufferedReader.ready()) cmd = bufferedReader.readLine();
                else break;

                //проверка рекурсии
                StringTokenizer executeChecker = new StringTokenizer(cmd);
                if (executeChecker.nextToken().toLowerCase().equals("execute_script"))
                {
                    boolean recurtion_checker = true;
                    StringTokenizer values = new StringTokenizer(command);
                    String checker = executeChecker.nextToken();
                    while (values.hasMoreTokens()){
                        if (values.nextToken().equals(checker) && human.size() == 0)
                        {
                            cmd = bufferedReader.readLine();
                            recurtion_checker = false;
                            break;
                        }
                    }
                    if (recurtion_checker) {
                        String command_and_paths = "execute_script";
                        values = new StringTokenizer(command);
                        values.nextToken();
                        while (values.hasMoreTokens())
                            command_and_paths += " " + values.nextToken();
                        executeChecker = new StringTokenizer(cmd);
                        executeChecker.nextToken();
                        command_and_paths += " " + executeChecker.nextToken();
                        cmd = command_and_paths;
                    }
                }

                human = commandShell.Analyze(humanList, human, cmd, b);
                humanList.setHumanBeings(Converter.convertToList(human));
            }
        } catch (FileNotFoundException e){ System.out.println("Данного файла не существует");
        } catch (IOException e) { System.out.println("Не удалось считать из файла");
        } //catch (RepeatException e) { System.out.println("Произошел рекурсивный вызов скрипта"); }
        return human;
    }
}
