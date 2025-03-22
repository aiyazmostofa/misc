import discord
from io import StringIO
from contextlib import redirect_stdout


class Client(discord.Client):
    async def on_ready(self):
        print('Ready!')

    async def on_message(self, message):
        if message.author != self.user:
            return

        content = str(message.content)
        if content.startswith("$py\n") and content.endswith("\n$py"):
            code = content[content.index("$py\n")+4:content.rindex("\n$py")]
            f = StringIO()
            with redirect_stdout(f):
                exec(code)
            output = f.getvalue()
            if output is None or len(output) == 0:
                return
            await message.channel.send(output)


client = Client()
f = open("token")
client.run(f.readline())
f.close()
