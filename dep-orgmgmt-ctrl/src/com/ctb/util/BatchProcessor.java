package com.ctb.util;

import java.util.*;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.UnboundedFifoBuffer;

/*
 * 
 * @author  Tata Consultancy Services
 * 
 */



/**
 * <p>This class is a generic Async Processor framework.  It is instantiated with a fixed number of Threads.
 *   It also contains a very simple installation of a prioity queue.  Each time a thread wakes up it
 * picks a command off the priority queue and runs it.</p>
 *
 * <p>Processes placed in the queue will be handled based on their priority.  Priorities run from
 * 1(lowest) to 10(highest),  To use this framework first call BatchProcessor.init().  This will start the processor threads.
 * Then add processes to the queue with BatchProcessor.addProcessToQueue(Process).  The processes will be picked up by
 * the threads in order of priority.  When shutting down call BatchProcessor.destroy().  All processes in
 * the queue wukk be discarded.</p>
 *
 */
public class BatchProcessor {
   

    public static int PRIORITY_MINIMUM=1;
    public static int PRIORITY_NORMAL=5;
    public static int PRIORITY_MAXIMUM=10;
    
    private int WAIT_SEC=60;        // default to 1 minute
    private int NUM_PROCESSES = 3;  // default to 3 threads
    public static String BATCH_PROCESSOR = "BatchProc";
    private Thread processorThread[] = new ProcessorThread[NUM_PROCESSES];
    private int numBatchProcessorsStarted =0;
    private Buffer q = null;
    private Map activeProcesses = null;
    
    
    protected BatchProcessor(){
        UnboundedFifoBuffer unboundedFifoBuff = new UnboundedFifoBuffer();
        q = BufferUtils.synchronizedBuffer(unboundedFifoBuff);
        activeProcesses = Collections.synchronizedMap(new TreeMap());
    
    }
    

    private static BatchProcessor instance;
    public static BatchProcessor getInstance() {
        if (instance==null) throw new IllegalStateException("BatchProcessor not initialized");
        return instance;
    }
    /**
     * <p>Initialize the BatchProcessor.  If the Properties is null default values will be used.
     * parameters that can be in the Properties are :</p>
     * <ul>
     *   <li>com.ctb.util.BatchProcessor.WAIT_SEC=[integer number of seconds between thread wake-ups] default in 60</li>
     *   <li>com.ctb.util.BatchProcessor.NUM_PROCESSES=[integer number of threads] default is 3</li>
     *   <li>com.ctb.util.BatchProcessor.PROCESSOR_NAME=[string name of thread for logging] default is "BatchProc"</li>
     * </ul>
     *
     * @param p Properties file containing the initialization parameter.
     * @throws IllegalStateException If the processor is already initialized.
     */
    public static void init() throws IllegalStateException {
       // if (instance!=null) throw new IllegalStateException("BatchProcessor is already initialized.");
        instance = new BatchProcessor();
       /*if (p!=null) {
            //PropertiesHelper ph = new PropertiesHelper(p);
            instance.WAIT_SEC = 60;//ph.getIntegerProperty("com.ctb.util.BatchProcessor.WAIT_SEC", 60);
            instance.NUM_PROCESSES =3;// ph.getIntegerProperty("com.ctb.util.BatchProcessor.NUM_PROCESSES", 3);
            instance.BATCH_PROCESSOR = "BatchProc";// ph.getProperty("com.ctb.util.BatchProcessor.PROCESSOR_NAME", "BatchProc");
        }*/
        instance.start();
    }
    /**
     * Interupts all threads and sets the instance to null.
     */
    public static void destroy() {
        if (instance==null) return;
        instance.stop();
        instance = null;
    }
    /**
     * Add a process to the queue.
     *
     * @param p A subclasses of the abstract BatchProcessor.Process class with the run() method defined.
     * @throws IllegalStateException If the processor is not initialized.
     */
    public static void addProcessToQueue(Process p) throws IllegalStateException {
        init();
        if (instance==null) throw new IllegalStateException("BatchProcessor must be initialized witht he init(Properties) method.");
        instance.q.add(p);
    }
    /**
     * Return the number of Processes currently waiting in the queue.
     * This could be improved to include the number of processes active as well.
     * @return integer number of processes.
     * @throws IllegalStateException If the processor is not initialized.
     */
    public static int numQueuedProcesses() throws IllegalStateException  {
        if (instance==null) throw new IllegalStateException("BatchProcessor must be initialized with the init(Properties) method.");
        return instance.q.size();
    }
    /**
     * Return all the queued processes in the Queue
     * @return integer number of processes.
     * @throws IllegalStateException If the processor is not initialized.
     */
    public static List getQueuedProcesses() throws IllegalStateException  {
        if (instance==null) throw new IllegalStateException("BatchProcessor must be initialized with the init(Properties) method.");
        List l = new LinkedList();
        synchronized (instance.q) {
            for (Iterator itProcs = instance.q.iterator(); itProcs.hasNext();) {
                l.add(String.valueOf(itProcs.next()));
            }
        }
        return l;
    }
    /**
     * Return all the running processes in the Queue
     * @return integer number of processes.
     * @throws IllegalStateException If the processor is not initialized.
     */
    public static List getRunningProcesses() throws IllegalStateException  {
        if (instance==null) throw new IllegalStateException("BatchProcessor must be initialized with the init(Properties) method.");
        List l = new LinkedList();
        synchronized (instance.activeProcesses) {
            for (Iterator itThreads = instance.activeProcesses.values().iterator(); itThreads.hasNext();) {
                l.add(itThreads.next());
            }
        }
        return l;
    }

    

    
    protected void start() {
        if (numBatchProcessorsStarted == 0) {
            
            for(int i=0; i<NUM_PROCESSES; i++) {
                processorThread[i] = new ProcessorThread();
                processorThread[i].setPriority(Thread.currentThread().getPriority()-1);
                processorThread[i].setName(BATCH_PROCESSOR + incrementNumQueueProcessorsStarted());
                processorThread[i].start();
            }
        }
    }
    protected void stop() {
        for(int i=0; i<NUM_PROCESSES; i++) {
            processorThread[i].interrupt();
        }
    }

    private synchronized int incrementNumQueueProcessorsStarted() {
        return ++numBatchProcessorsStarted;
    }


    private class ProcessorThread extends Thread {
        protected void setActivity(String activity) {
            activeProcesses.put(getName(), "P" + getPriority() + " " + getName() + " -> " + activity);
        }

        public ProcessorThread() {
            super();
        }
        public void run() {
            setActivity("Sleeping");
            try {
                while(!interrupted()) {
                    try {
                        Process p = null;
                        synchronized (q) {
                            if (q.size()>0)
                                p = (Process)q.remove();
                        }
                        // if there is something to do -- do it!
                        if (p!=null) {
                            setActivity(String.valueOf(p));
                            p.run();
                            setActivity("Sleeping");
                        }
                    } catch (Exception e) {
                       
                    }
                    Thread.sleep(WAIT_SEC * 1000); // x sec * 1000 millis
                }
            } catch (InterruptedException e) {
//                log.debug(e.toString(), e);
            }
            activeProcesses.remove(getName());
        }
    }

    public static abstract class Process implements Runnable, Comparable {
        private int priority=PRIORITY_NORMAL;  // assume values from 1 to 10 with smaller numbers being higher priority

        protected void setPriority(int priority) throws IllegalArgumentException {
            if (priority<1 || 10 <priority) throw new IllegalArgumentException("Priority must be on the scale 1(lowest) to 10(highest).");
            this.priority = priority;
        }
        protected int getPriority() {
            return priority;
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * @param   o the Object to be compared.
         * @return  a negative integer, zero, or a positive integer as this object
         *		is less than, equal to, or greater than the specified object.
         *
         * @throws ClassCastException if the specified object's type prevents it
         *         from being compared to this Object.
         */
        public int compareTo(Object o) throws ClassCastException {
            Process other = (Process)o;
            return this.priority-other.priority;
        }

        /**
         * Indicates whether some other object is "equal to" this one.
         *
         * @param   obj   the reference object with which to compare.
         * @return  <code>true</code> if this object is the same as the obj
         *          argument; <code>false</code> otherwise.
         */
        public boolean equals(Object obj) {
            if (obj instanceof Process) {
                return ((Process)obj).priority == this.priority;
            } else
                return false;

        }
    }
}
