package org.intrace.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.intrace.shared.AgentConfigConstants;

/**
 * Args Format: "[arg1[arg2[arg3"
 * 
 * where argx is of the form value-parameter
 */
public class AgentSettings
{
  // Static settings which control agent startup
  private int serverPort = 9123;
  private int callbackPort = -1;
  private boolean waitStart = false;
  
  // Static state  
  private int actualServerPort = -1;
  
  // Dynamic settings
  private Pattern classRegex = Pattern.compile("^$");
  private Pattern excludeClassRegex = Pattern.compile("^$");
  private boolean instruEnabled = true;
  private boolean saveTracedClassfiles = false;
  private boolean verboseMode = false;  

  public AgentSettings(String args)
  {
    parseArgs(args);
  }

  public AgentSettings(AgentSettings oldInstance)
  {
    // Copy all static state and dynamic settings
    actualServerPort = oldInstance.getActualServerPort();
    classRegex = oldInstance.getClassRegex();
    excludeClassRegex = oldInstance.getExcludeClassRegex();
    instruEnabled = oldInstance.isInstrumentationEnabled();
    saveTracedClassfiles = oldInstance.saveTracedClassfiles();
    verboseMode = oldInstance.isVerboseMode();
  }

  public void parseArgs(String args)
  {
    String[] seperateArgs = args.split("\\[");
    for (int ii = 0; ii < seperateArgs.length; ii++)
    {
      parseArg("[" + seperateArgs[ii]);
    }
  }

  private void parseArg(String arg)
  {
    if (arg.toLowerCase().equals(AgentConfigConstants.VERBOSE_MODE + "true"))
    {
      verboseMode = true;
    }
    else if (arg.toLowerCase().equals(
                                      AgentConfigConstants.VERBOSE_MODE
                                          + "false"))
    {
      verboseMode = false;
    }
    else if (arg.toLowerCase().startsWith(AgentConfigConstants.OPT_SERVER_PORT))
    {
      String serverPortStr = arg.replace(AgentConfigConstants.OPT_SERVER_PORT, "");
      serverPort = Integer.parseInt(serverPortStr);
    }
    else if (arg.toLowerCase().startsWith(AgentConfigConstants.CALLBACK_PORT))
    {
      String callbackPortStr = arg.replace(AgentConfigConstants.CALLBACK_PORT, "");
      callbackPort = Integer.parseInt(callbackPortStr);
    }
    else if (arg.toLowerCase().equals(
                                      AgentConfigConstants.INSTRU_ENABLED
                                          + "true"))
    {
      instruEnabled = true;
    }
    else if (arg.toLowerCase().equals(
                                      AgentConfigConstants.INSTRU_ENABLED
                                          + "false"))
    {
      instruEnabled = false;
    }
    else if (arg.toLowerCase()
                .equals(AgentConfigConstants.SAVE_TRACED_CLASSFILES + "true"))
    {
      saveTracedClassfiles = true;
    }
    else if (arg.toLowerCase()
                .equals(AgentConfigConstants.SAVE_TRACED_CLASSFILES + "false"))
    {
      saveTracedClassfiles = false;
    }
    else if (arg.toLowerCase()
        .equals(AgentConfigConstants.START_WAIT))
    {
      waitStart = true;
    }
    else if (arg.toLowerCase().equals(AgentConfigConstants.START_ACTIVATE))
    {
      waitStart = false;
    }
    else if (arg.startsWith(AgentConfigConstants.CLASS_REGEX))
    {
      String classRegexStr = arg.replace(AgentConfigConstants.CLASS_REGEX, "");
      classRegex = Pattern.compile(classRegexStr);
    }
    else if (arg.startsWith(AgentConfigConstants.EXCLUDE_CLASS_REGEX))
    {
      String classExcludeRegexStr = arg
                                       .replace(
                                                AgentConfigConstants.EXCLUDE_CLASS_REGEX,
                                                "");
      excludeClassRegex = Pattern.compile(classExcludeRegexStr);
    }
  }

  public boolean isWaitStart()
  {
    return waitStart;
  }

  public int getActualServerPort()
  {
    return actualServerPort;
  }

  public void setActualServerPort(int xiActualServerPort)
  {
    actualServerPort = xiActualServerPort;
  }

  public int getServerPort()
  {
    return serverPort;
  }

  public int getCallbackPort()
  {
    return callbackPort;
  }

  public Pattern getClassRegex()
  {
    return classRegex;
  }

  public Pattern getExcludeClassRegex()
  {
    return excludeClassRegex;
  }

  public boolean isInstrumentationEnabled()
  {
    return instruEnabled;
  }

  public boolean saveTracedClassfiles()
  {
    return saveTracedClassfiles;
  }

  public boolean isVerboseMode()
  {
    return verboseMode;
  }

  @Override
  public String toString()
  {
    // Output key settings
    String currentSettings = "";
    currentSettings += "Class Regex                : " + classRegex + "\n";
    currentSettings += "Exclude Class Regex        : " + excludeClassRegex + "\n";
    currentSettings += "Tracing Enabled            : " + instruEnabled + "\n";
    currentSettings += "Save Traced Class Files    : " + saveTracedClassfiles
                       + "\n";
    return currentSettings;
  }

  public Map<String, String> getSettingsMap()
  {
    Map<String, String> settingsMap = new HashMap<String, String>();
    settingsMap.put(AgentConfigConstants.INSTRU_ENABLED,
                    Boolean.toString(instruEnabled));
    settingsMap.put(AgentConfigConstants.CLASS_REGEX, classRegex.pattern());
    settingsMap.put(AgentConfigConstants.EXCLUDE_CLASS_REGEX,
                    excludeClassRegex.pattern());
    settingsMap.put(AgentConfigConstants.VERBOSE_MODE,
                    Boolean.toString(verboseMode));
    settingsMap.put(AgentConfigConstants.SAVE_TRACED_CLASSFILES,
                    Boolean.toString(saveTracedClassfiles));
    settingsMap.put(AgentConfigConstants.SERVER_PORT, Integer.toString(actualServerPort));
    settingsMap.put(AgentConfigConstants.START_WAIT, Boolean.toString(waitStart));
    return settingsMap;
  }
}
