package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.Feature;

public class OthersCommandRegister extends Feature {
    @Override
    protected void doRegister() {
//        new BbCommand().register();
        new CloneCommand().register();
        new FullWhitelistCommand().register();
        new GiveChakraCommand().register();
        new GiveItemCommand().register();
        new InvseeCommand().register();
        new MeditationCommand().register();
        new NinkenCommand().register();
        new ParcheminCommand().register();
        new ReloadDBCommand().register();
        new JutsuCommand().register();
        new StealChakraCommand().register();
        new StopCommand().register();
        new TargetCommand().register();
        new TPBackCommand().register();
        new TransferCommand().register();
        new WhoIsCommand().register();
    }
}
