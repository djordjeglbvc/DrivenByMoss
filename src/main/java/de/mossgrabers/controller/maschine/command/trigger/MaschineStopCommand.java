// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.command.trigger;

import de.mossgrabers.controller.maschine.MaschineConfiguration;
import de.mossgrabers.controller.maschine.controller.MaschineControlSurface;
import de.mossgrabers.framework.command.trigger.transport.StopCommand;
import de.mossgrabers.framework.controller.display.ITextDisplay;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.ITransport;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.ViewManager;
import de.mossgrabers.framework.view.Views;


/**
 * Command handle the stop button.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MaschineStopCommand extends StopCommand<MaschineControlSurface, MaschineConfiguration>
{
    private boolean consumed;


    /**
     * Constructor.
     *
     * @param model The model
     * @param surface The surface
     */
    public MaschineStopCommand (final IModel model, final MaschineControlSurface surface)
    {
        super (model, surface);
    }


    /** {@inheritDoc} */
    @Override
    public void executeNormal (final ButtonEvent event)
    {
        final ViewManager viewManager = this.surface.getViewManager ();

        if (event == ButtonEvent.DOWN)
        {
            if (!viewManager.isActiveView (Views.SHIFT))
                viewManager.setActiveView (Views.SHIFT);
            return;
        }

        if (event != ButtonEvent.UP)
            return;

        if (viewManager.isActiveView (Views.SHIFT))
            viewManager.restoreView ();

        if (this.consumed)
        {
            this.consumed = false;
            return;
        }
        final ITransport transport = this.model.getTransport ();
        if (transport.isPlaying ())
            this.handleStopOptions ();
        else
            transport.stopAndRewind ();

        this.surface.clearCache ();
        ((ITextDisplay) this.surface.getDisplay ()).forceFlush ();
    }


    /**
     * Signal that the stop function should not be called on button release.
     */
    public void setConsumed ()
    {
        this.consumed = true;
    }
}
