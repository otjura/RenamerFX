# RenamerFX  
Tool for batch renaming files. Recurses directory tree and does a text replace on filenames.  

## Build & Run  
Requirements:  
* Java 11  
* Maven  

This is a Maven project not tied to any IDE. Executing `mvn clean javafx:run` in project root should suffice to run it. JavaFX comes in Maven config.  

`mvn build` creates a self-contained executable uberjar in ./target.  

## Use  
Clicking uberjar or running it without arguments from command-line opens the tool in graphical mode. Giving it arguments (below) runs it in command-line mode.  

```
USAGE
renamerfx <folder> <stringToReplace> <replacementString>

--help for this help
--interactive for interactive mode
```

## Disclaimer  
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  
SOFTWARE.  
