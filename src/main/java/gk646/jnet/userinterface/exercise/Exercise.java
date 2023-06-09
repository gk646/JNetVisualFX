package gk646.jnet.userinterface.exercise;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;

import java.util.HashMap;

public enum Exercise {

    ONE("""
            Equality Operation
                            
            The first exercise is to create a network capable of
            emulating an equality check. Equality means the same
            as the "==" operator in many programming languages.
            If both arguments left and right are equal (the same)
            it returns 1, else 0.This includes the case 0 == 0
            which returns 1 because both sides are indeed equal.
            This brings us to the following truth-table below:
                            
               Left  |  Right  | Output
            ---------------------------
                0    |    0    |   1
                0    |    1    |   0
                1    |    0    |   0
                1    |    1    |   1
                
            Now you have to encode this information into 2 lists.
            One input list and a output list. Note that you will
            need a two-dimensional list for both, even though
            there is only 1 element each in the output. This also
            gives some constraints about the Network you will have
            to create: It HAS to have 2 inputs, and 1 neuron at
            the output layer.
                        
            You can just copy this but pay attention that the
            index in the lists has to match:
                        
            So the pair 0,0 has to be at the same index as 0 in
            the output list.
                        
            Now finally create a network. The sigmoid function
            works well here. The other default values should
            do just fine. Start training the network with
            "jnet_train(<input>,<output>,300)" and watch both
            the forward and backward passes occurring.
            You should also see a decreasing error in the log.
                        
            It can happen that the network gets stuck or spins off
            while learning. Should the error not go towards 0 you
            should recreate the Network and train it again.
                        
            To confirm if its working use "jnet_out([0,1])"
            with any input pair and check if you get the
            correct output; "ex_test" to test it.
            """) {
        @Override
        public String toString() {
            return "1";
        }

        @Override
        public double test() {
            double error = 0;

            double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
            double[][] outputs = {{1}, {0}, {0}, {1}};

            for (int i = 0; i < inputs.length; i++) {
                error += Playground.neuralNetwork.out(inputs[i])[0] - outputs[i][0];
            }
            return error;
        }

        @Override
        void init() {
            hintMap.put(0, "The correct input list is: [[0,0][0,1][1,0][1,1]]");
            hintMap.put(1, "The correct output list is: [[1][0][0][1]]");
        }
    },

    TWO("""
            The XOR Gate
                        
            The X stands for exclusive; so in some way
            its a reversed OR, meaning its 1 if there is
            only one 1 in the input. The special case
            of 1,1 is however 0 since the are the same.
                        
            Figure out the truth table and make a network.
                        
                 Left  |  Right  | Output
            ---------------------------
                ?    |    ?    |   0
                ?    |    ?    |   1
                ?    |    ?    |   1
                ?    |    ?    |   0
                
            This time also experiment with different
            network sizes and parameters. Find out whats
            the bare minimum of learn cycles needed
            with any configuration iterations.
             """) {
        @Override
        public String toString() {
            return "2";
        }

        @Override
        public double test() {
            double error = 0;

            double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
            double[][] outputs = {{0}, {1}, {1}, {0}};

            for (int i = 0; i < inputs.length; i++) {
                error += Playground.neuralNetwork.out(inputs[i])[0] - outputs[i][0];
            }
            return error;
        }

        @Override
        void init() {
            hintMap.put(0, "0,1 outputs 1");
            hintMap.put(1, "0,0 outputs 0");
            hintMap.put(2, "Just for solving it you should be able to use the same setup as for exercise 1");
        }
    };
    final HashMap<Integer, String> hintMap = HashMap.newHashMap(5);
    final String info;
    int hintCounter;

    Exercise(String info) {
        this.info = info;
        init();
    }

    abstract void init();

    public abstract double test();

    public void resetHints() {
        this.hintCounter = 0;
    }

    public void getHint() {
        String s = hintMap.get(hintCounter++);
        if (s != null) {
            Terminal.addText(s);
        }
        Terminal.addText("no more hints!");
    }
}
