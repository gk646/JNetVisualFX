<!--suppress CheckImageSize -->
<img src="screenshots/1.0_Main.png" alt="The startup screen with a 10,10,10,2 Layer Network" width="830" height="580">


## For a comprehensive guide check [the Wiki!](https://github.com/gk646/JNetVisualFX/wiki/Home/)

**Disclaimer**:
*Iam in no way an expert on machine learning or neural networks. I do not guarantee that all information provided or gained through using JNetVisualFX is right. I made this application to learn about these topics myself by implementing them.*


## JNetVisualFX
JNetVisualFX is a terminal based NeuralNetwork visualizer with the focus on testing, training and playing around with *Neural Networks*. In the end its also meant as an educational application.


### **How to Install**

**Windows**  
The application comes already prepackaged in a .zip-archive with a runtime included. Just unzip it and start `JNetVisualFX.exe`.  **[Download](https://github.com/gk646/JNetVisualFX/releases/download/v.1.0.0/JNetVisualFX.zip)**

**Unix**  
Downlaod the v.x.x.x-LINUX.jar and run it. **You will need a basic Java Runtime (19) installed**.  

###  Features  

- Intuitive controls through a custom command line
- 35 different commands ranging from `print()`, `theme`, `set`,`jnet_train()`, to `getStat`.
- **Visual Training:** While training each actively used connection is visualized for both forward pass and backpropagation
- Each connection is rendered dynamically based on the correlating weight
- **Guided exercises** and lots of information inside the application 
- Local Save and User Statistics: Look up how many commands you used!

### **How to get started:**

Upon starting, you won't have a built network yet. For building your first Network you need a NetBuilder, which is a reusable building block for your network.    
Create a new NetBuilder with `new NetBuilder([2,2,1],sigmoid)`, the first argument being a list of numbers representing the neuronCount each layer. Second is a activationFunction.
You can customize your NetBuilder with further commands to your liking.  
When you have your desired NetBuilder call `new Network` to create a new Network with your NetBuilder.  
Voilà, you made your first Network now you can play around with it!

### **Terminal**

The terminal is the central part of JNetVisualFX allowing you to get information, customize the application and interact with networks.  
It supports:
- Adding and deleting characters
- Scrolling through past commands using UP and DOWN (if there's no autocompletion)
- changing cursor position with LEFT and RIGHT  
- Code completion with TAB (if there's only 1 suggestion)
- Pasting text from the clipboard
- Parsing of arithmetic expressions from the command line e.g `2+3`
- Printing variables (lists) with `$<var-name>` 


There's lots of useful commands but these here will get you started:
- `help` - displays helpful information 
- `helpall` - lists all commands in the log
- `man` - used as prefix for methods to display their manual page e.g. `man print`

Feel free to experiment around there's a lot of small commands to explore.

