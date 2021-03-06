/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unijena.cheminf.myfirstspringmvc.nplikeness.moleculecuration;

import org.openscience.cdk.interfaces.IAtomContainer;
import de.unijena.cheminf.myfirstspringmvc.nplikeness.misc.UUIDTagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Convenient class to do molecule curation
 *
 * @author kalai
 */
public class MoleculeCuration {

    UUIDTagger uuidTagger = null;
    MoleculeConnectivityChecker connectivityChecker = null;
    SugarRemover sugarRemover = null;
    StrangeElementsCurator strangeElementsCurator = null;

    public MoleculeCuration() {
        uuidTagger = new UUIDTagger();
        connectivityChecker = new MoleculeConnectivityChecker();
        sugarRemover = new SugarRemover();
        strangeElementsCurator = new StrangeElementsCurator();
    }

    public List<IAtomContainer> curateMolecule(IAtomContainer container){

        List<IAtomContainer> curatedMoleculeList = new ArrayList<IAtomContainer>();

        IAtomContainer taggedMolecule = uuidTagger.tagUUID(container);
        Map<Object, Object> properties = container.getProperties();
        List<IAtomContainer> connectivityCheckedMolecule = connectivityChecker.checkConnectivity(taggedMolecule);
        List<IAtomContainer> sugarsRemovedMolecule = sugarRemover.removeSugars(connectivityCheckedMolecule);
        List<IAtomContainer> strangeElementsRemovedMolecule = strangeElementsCurator.removeStrangeElements(sugarsRemovedMolecule);
        for (IAtomContainer mol : strangeElementsRemovedMolecule) {
            mol.setProperties(properties);
            curatedMoleculeList.add(mol);
        }
        return curatedMoleculeList;

    }
}
