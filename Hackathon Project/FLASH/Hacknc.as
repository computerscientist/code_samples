package
{

	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.net.URLRequestHeader;
	import flash.net.URLRequestMethod;
	
	public class Hacknc extends MovieClip
	{
		public static var URL:String = "http://1489c88d.ngrok.com/";
		
		public static var sideLength:int = 30;
		public static var json:JSON;
		
		private var jsonURL:String = URL + "data.json";
		
		public function Hacknc()
		{
			this.addEventListener(Event.ENTER_FRAME, onFrame);
			this.addEventListener(Event.EXIT_FRAME, onExitFrame);
		}
		
		private function onFrame(e:Event):void {
			var request:URLRequest=new URLRequest();
			request.url=this.jsonURL; 
			request.requestHeaders=[new  URLRequestHeader("Content-Type", "application/json")];
			request.method= URLRequestMethod.GET;
			var loader:URLLoader=new URLLoader();
			loader.addEventListener(Event.COMPLETE, readJSON);
			loader.load(request);
			
			var loader:URLLoader = new URLLoader();
			var request:URLRequest = new URLRequest();
			request.url = this.jsonURL;
			loader.addEventListener(Event.COMPLETE, readJSON);
			loader.load(request);
			
		}
		
		private function onExitFrame(e:Event):void {
			writeJSON();
		}
		
		private function readJSON(e:Event):void {
			var loader:URLLoader = URLLoader(e.target);
			var jsonArray:Array = JSON.decode(loader.data);	
		}
		
		private function writeJSON(e:Event):void {
			
		}
		
	}
}