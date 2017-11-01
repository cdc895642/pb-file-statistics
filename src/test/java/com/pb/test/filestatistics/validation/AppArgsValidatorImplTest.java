package com.pb.test.filestatistics.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.pb.test.filestatistics.service.FileStatisticsServiceImpl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AppArgsValidatorImplTest {

  @Mock
  private ApplicationArguments appArgs;
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @InjectMocks
  private AppArgsValidatorImpl appArgsValidator;

  @Before
  public void setup() throws IOException {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isValid_emptyArgs_returnFalse() throws IOException {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<>());

    //Act
    boolean result=appArgsValidator.isValid(appArgs);

    //Assert
    assertFalse(result);
  }

  @Test
  public void isValid_nullArgs_returnFalse() throws IOException {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(null);

    //Act
    boolean result=appArgsValidator.isValid(appArgs);

    //Assert
    assertFalse(result);
  }

  @Test
  public void isValid_setFile_returnFalse() throws IOException {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      add(temporaryFolder.newFile().getPath());
    }});

    //Act
    boolean result=appArgsValidator.isValid(appArgs);

    //Assert
    assertFalse(result);
  }

  @Test
  public void isValid_setNotExistedFolder_returnFalse() throws IOException {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      add(temporaryFolder.newFolder().getPath()+ File.separator+"FAKE_FOLDER");
    }});

    //Act
    boolean result=appArgsValidator.isValid(appArgs);

    //Assert
    assertFalse(result);
  }

  @Test
  public void isValid_setFolderSeparatorInTheEnd_returnTrue() throws IOException {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      add(temporaryFolder.newFolder().getPath()+ File.separator);
    }});

    //Act
    boolean result=appArgsValidator.isValid(appArgs);

    //Assert
    assertTrue(result);
  }

  @Test
  public void isValid_setFolder_returnTrue() throws IOException {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      add(temporaryFolder.newFolder().getPath());
    }});

    //Act
    boolean result=appArgsValidator.isValid(appArgs);

    //Assert
    assertTrue(result);
  }
}
