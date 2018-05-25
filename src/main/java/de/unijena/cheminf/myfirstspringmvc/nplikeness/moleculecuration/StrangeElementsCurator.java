/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unijena.cheminf.myfirstspringmvc.nplikeness.moleculecuration;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


/**
 * Class to remove molecules containing elements other than non-metals
 *
 * @author kalai
 */


public class StrangeElementsCurator {

    private final String[] check = {"C", "H", "N", "O", "P", "S", "Cl", "F", "As", "Se", "Br", "I", "B"};
    private final HashSet<String> symbols2Check = new HashSet<String>(Arrays.asList(check));

    public List<IAtomContainer> removeStrangeElements(List<IAtomContainer> moleculeSet) {

        List<IAtomContainer> curated = new ArrayList();
        if (!moleculeSet.isEmpty()) {
            for (IAtomContainer mol : moleculeSet) {
                if (!shouldRemoveMolecule(mol)) {
                    curated.add(mol);
                }
            }
        }
        return curated;
    }

    private boolean shouldRemoveMolecule(IAtomContainer molecule) {
        for (IAtom atom : molecule.atoms()) {
            if (!symbols2Check.contains(atom.getSymbol())) {
                System.out.println("contains strange");
               return true;
            }
        }
        return false;
    }
}
