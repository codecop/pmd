/*
 * User: tom
 * Date: Aug 22, 2002
 * Time: 5:13:14 PM
 */
package net.sourceforge.pmd.dcpd;

import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.sourceforge.pmd.cpd.*;

import java.rmi.RemoteException;
import java.rmi.MarshalledObject;
import java.util.*;

public class DCPDWorker {

    private JavaSpace space;
    private Map jobs = new HashMap();

    public DCPDWorker(JavaSpace space) {
        try {
            this.space = space;
            // register for future jobs
            space.notify(new Job(), null, new JobAddedListener(space, this), Lease.FOREVER, null);
            // get a job if there are any out there
            Job job = (Job)space.readIfExists(new Job(), null, 200);
            if (job != null) {
                jobAdded(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jobAdded(Job job) {
        try {
            System.out.println("Received a job " + job.name + ", id is " + job.id.intValue());
            if (!jobs.containsKey(job)) {
                TokenSetsWrapper tsw = (TokenSetsWrapper)space.read(new TokenSetsWrapper(null, job), null, 100);
                System.out.println("Read a TokenSetsWrapper with " + tsw.tokenSets.size() + " token lists");
                jobs.put(job, tsw);
            }

            while (true) {
                TileExpander te = new TileExpander(space, jobs);
                te.expandAvailableTiles();
                Thread.currentThread().yield();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new DCPDWorker(Util.getInstance().findSpace(Util.getInstance().getSpaceServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
