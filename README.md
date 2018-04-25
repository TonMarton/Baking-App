# Baking-App
Android Application for one of the projects at Android Developer Nanodegree by Google

<h3>Contents</h3>
<ul>
  <li><a href="#overview">Overview</a></li>  
  <li><a href="#specifications">Specifications for the project</a></li>
  <li><a href="#technical_details">Technical details</a></li>
</ul>

<h3 id="overview">Overview</h3>

  <p>I applied for a Google Developer Scholarship in October 2017 (<a href="https://www.udacity.com/grow-with-google" target="_blank">scholarship site</a>). There were 4 paths available, and since I have already completed Udacity's Android Basics Nanodegree by this time, I wanted to take get enrolled into the Android Developer Track.</p>
  <p> So I did, and completed the Challenge Course over the 3 months. 1,500 students got selected from the ones who managed to complete this short course in time and I was luckily one of them, so I got full access to the Android Devloper Nanodegree (<a href="https://eu.udacity.com/course/android-developer-nanodegree-by-google--nd801" target="_blank">nanodegree site</a>)</p>
  <p>This is one of the projects I had to complete to finish the scholarship. Some guidelines were provided and also some very accurate details about some features I had to implement (<a href="#specifications">see them here</a>), but I had to start building from scratch. And after about three weeks of work (with some huge gaps in it), my work got approved.</p>
  
<h3 id="specifications">The specifiacations to meet for this project: </h3>

  <h6>Make A Baking App</h6>
  
  <p>Data source: <a href="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json" target="_blank">https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json</a></p>

  <ul>
  <li>App is written solely in the Java Programming Language</li>

  <li>App should display recipes from provided network resource.</li>

  <li>App should allow navigation between individual recipes and recipe steps.</li>

  <li>App uses RecyclerView and can handle recipe steps that include videos or images.</li>

  <li>App conforms to common standards found in the Android Nanodegree General Project Guidelines.</li>

  <li>Application uses Master Detail Flow to display recipe steps and navigation between them.</li>

  <li>Application uses Exoplayer to display videos.</li>

  <li>Application properly initializes and releases video assets when appropriate.</li>

  <li>Proper network asset utilization</li>

  <li>Application should properly retrieve media assets from the provided network links. It should properly handle network requests.</li>

  <li>Application makes use of Espresso to test aspects of the UI.</li>

  <li>Application sensibly utilizes a third-party library to enhance the app's features. That could be helper library to interface with ContentProviders if you choose to store the recipes, a UI binding library to avoid writing findViewById a bunch of times, or something similar.</li>

  <li>Application has a companion homescreen widget.</li>

  <li>Widget displays ingredient list for desired recipe.</li>
  </ul>  
  
  <h3 id="technical_details">Technical details (<i>and my own reflection</i>)</h3>
  <h4>MainActivity.java</h4>
  <p>Here I had to make a RecyclerView, which gets data by an AsyncTaskLoader through an Adapter. Because the api provided was not so complete, I had to use placeholder images. Apart from that it was straight forward and I believe that I wrote clean and good code.</p>
  <p><i>screenshots:</i></p>
  
   | Handset - portrait     |   Handset - landscape  | Tablet - landscape |
   |:---:|:---:|:---:|
   |<img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/mainactivity_handset_portrait.png" width="200"/> | <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/mainactivity_handset_landscape.png" width="200"/> | <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/mainactivity_tablet_landscape.png" width="200"> |
   
   <h4>RecipeActivity.java</h4>
  <p>This was the main struggle in the design process, I didn't know which way to go with the fragments on different screen sizes and orientations. Finally, I decided that I will implement custom animations and reuse the same fragment as much as possible. This way the app doesen't have to define a separate activity each time the user switches over to another recipe step. Advantages of this approach are that the animations and layouts are quite nice for every screen size, without much customization and alternative layouts, nearly no extra code for tablet layouts, better performance (since the fragment gets created only once). But on the other hand, I sacraficed a bit of code readablity and consizeness, so I will definetely not follow this route, or at least I will be more careful when writing long if else statments to determine the screen size, etc. This was my first time using fragments to such an extent and my very first time using exoPlayer library.</p>
  <p><i>screenshots:</i></p>
  
   <h6>MasterListFragment</h6>
   
   | Handset - landscape     |   Handset - portrait  | Tablet - portrait |
   |:---:|:---:|:---:|
   | <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/master_list_fragment_handset_landscape.png" width="200"/> | <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/master_list_fragment_handset_portrait.png" width="200"/> | <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/master_list_fragment_tablet_portrait.png" width="200"> |
   
   <h6>StepDetailFragment</h6>
   
   | Handset - landscape     |   Handset - portrait  | Tablet - portrait |
   |:---:|:---:|:---:|
  |<img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/recipe_detail_fragment_handset_portrait.png" width="200"/> | <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/recipe_detail_fragment_handset_landscape.png" width="200"/> | <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/recipe_detail_fragment_tablet_portrait.png" width="200"> |
   
   <h6>Both fragments displayed constantly</h6>
 <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/recipeactivity_tablet_landscape.png" width="200"/>
 
 <h4>Widget</h4>
 <p>I had some struggle in the beginning with the widget, since this was my first one. But than it went all fine. I sadly realised that the Adapters, Intents and even Parcelable objects have their own ways when it comes to widgets.</p>
 
   <p><i>screenshots:</i></p>
 <img src="https://github.com/TonMarton/Baking-App/blob/master/Screenshots/widget_handset.png" width="200"/>
